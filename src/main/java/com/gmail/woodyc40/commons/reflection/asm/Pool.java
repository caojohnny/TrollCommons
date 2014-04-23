package com.gmail.woodyc40.commons.reflection.asm;

import com.gmail.woodyc40.commons.reflection.asm.sun.ClassFileAssembler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pool {
    private final List<ConstantImpl<?>> constantList = new ArrayList<>();

    private final Constant<String> utfConst = new Constant<String>(1) {
        @Override
        void out(DataOutputStream stream, String string) throws IOException {
            stream.writeUTF(string);
        }
    };

    private final Constant<Integer> classInfoConst = new Constant<Integer>(7) { // Special case: int takes position
        // on constantpool
        @Override
        void out(final DataOutputStream stream, final Integer integer) throws IOException {
            stream.writeShort(integer.intValue());
        }
    };

    private int toList(ConstantImpl<?> impl) {
        constantList.add(impl);
        return constantList.size();
    }

    public int newUTF(String utf) {
        return toList(new ConstantImpl<>(utfConst, utf));
    }

    public int newClassInfo(String className) {
        int name = newUTF(className.replace(".", "/"));
        return toList(new ConstantImpl<>(classInfoConst, Integer.valueOf(name)));
    }

    public void debug() {
        System.out.println(constantList.size());
    }

    public void writeData(ClassFileAssembler assembler) throws IOException {
        assembler.emitShort((short) constantList.size());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream stream0 = new DataOutputStream(stream);
        for (ConstantImpl<?> impl : constantList) {
            assembler.emitShort((short) impl.template.tag);
            impl.write(stream0);
        }
        stream0.close();

        for (byte b : stream.toByteArray())
            assembler.emitByte(b);
        stream.close();
    }

    private class ConstantImpl<T> {
        private final T           t;
        private final Constant<T> template;

        public ConstantImpl(Constant<T> template, T t) {
            this.t = t;
            this.template = template;
        }

        public void write(DataOutputStream output) throws IOException {
            template.write(output, this.t);
        }
    }

    private abstract class Constant<T> {
        final int tag;

        public Constant(int tag) {
            this.tag = tag;
        }

        public void write(DataOutputStream stream, T t) throws IOException {
            stream.writeByte((byte) this.tag);
            out(stream, t);
        }

        abstract void out(DataOutputStream stream, T t) throws IOException;
    }

    public interface Attr {
        public void toData(ClassFileAssembler assembler);
    }

    public static class SourceAttr implements Attr {
        private final String tag = "SourceFile";
        private final Pool   def;
        private final byte[] data;

        public SourceAttr(Pool pool, String name) {
            this.def = pool;
            int index = pool.newUTF(name);
            byte[] bvalue = new byte[2];
            bvalue[0] = (byte) (index >>> 8);
            bvalue[1] = (byte) index;
            data = bvalue;
        }

        public void toData(ClassFileAssembler assembler) {
            assembler.emitShort((short) def.newUTF(tag));
            assembler.emitShort((short) data.length);
            for (byte b : data)
                assembler.emitByte(b);
        }
    }

    public static class CodeAttr implements Attr {
        private final String tag = "Code";
        private final Pool   def;
        private final byte[] data;

        private final int maxStack;
        private final int maxLocals;

        public CodeAttr(Pool pool, int stack, int locals, ClassFileAssembler assembler) {
            this.def = pool;

            maxStack = stack;
            maxLocals = locals;

            data = assembler.getData().getData();
        }

        public void toData(ClassFileAssembler assembler) {
            assembler.emitShort((short) def.newUTF(tag));

            int len = (18 + data.length + 0 * 8 /* Exception table length */
                    + 0/* Attribute length */) - 6;
            assembler.emitShort((short) len);
            assembler.emitShort((short) maxStack);
            assembler.emitShort((short) maxLocals);
            assembler.emitShort((short) data.length);
            for (byte b : data)
                assembler.emitByte(b);
            assembler.emitShort((short) 0); // Exception table length
            assembler.emitShort((short) 0); // Attributes
            for (byte b : data)
                assembler.emitByte(b);
        }
    }
}
