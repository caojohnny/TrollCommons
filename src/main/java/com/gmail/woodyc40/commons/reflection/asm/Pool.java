package com.gmail.woodyc40.commons.reflection.asm;

import com.gmail.woodyc40.commons.reflection.asm.sun.ClassFileAssembler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a constant pool in a bytecoded <code>class</code>
 * 
 * @author AgentTroll
 * @version 1.0
 */ 
public class Pool {
    /**
     * {@link java.util.List} of constants held by the pool
     */ 
    private final List<ConstantImpl<?>> constantList = new ArrayList<>();

    /**
     * The template for a UTF8 constant
     */ 
    private final Constant<String> utfConst = new Constant<String>(1) {
        @Override
        void out(DataOutputStream stream, String string) throws IOException {
            stream.writeUTF(string);
        }
    };

    /**
     * The template for an int constant
     */ 
    private final Constant<Integer> classInfoConst = new Constant<Integer>(7) { // Special case: int takes position
        // on constantpool
        @Override
        void out(final DataOutputStream stream, final Integer integer) throws IOException {
            stream.writeShort(integer.intValue());
        }
    };

    /**
     * Pushes a constant implementation to the constant pool
     * 
     * @param impl the impementation to push
     * @return the index of the latest push size of the pool
     */ 
    private int toList(ConstantImpl<?> impl) {
        constantList.add(impl);
        return constantList.size();
    }

    /**
     * Pushes a new UTF8 string to the constant pool
     * 
     * @param utf the string to add to the pool
     * @return the index of the string in the pool
     */ 
    public int newUTF(String utf) {
        return toList(new ConstantImpl<>(utfConst, utf));
    }

    /**
     * Addes <code>class</code> info referenced by the bytecode builder to the pool
     * 
     * @param className the fully qualified name of the <code>class</code>
     * @return the index of the info in the constant pool
     */ 
    public int newClassInfo(String className) {
        int name = newUTF(className.replace(".", "/"));
        return toList(new ConstantImpl<>(classInfoConst, Integer.valueOf(name)));
    }
    
    //TODO
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
