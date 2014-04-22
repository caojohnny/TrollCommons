package com.gmail.woodyc40.commons.reflection.asm;

import com.gmail.woodyc40.commons.reflection.asm.sun.ClassFileAssembler;

public class BytecodeMethod {
    private final int    access;
    private final String name;
    private final String desc;

    private Pool.Attr attr;

    public BytecodeMethod(String methodName, int access, Class<?> ret, Class[] params) {
        this.access = access;
        this.name = methodName;
        this.desc = Descriptors.descMethod(ret, params);
    }

    public void addCode(Pool.CodeAttr attr) {
        this.attr = attr;
    }

    public void add(ClassBytecode bytecode) {
        ClassFileAssembler assembler = bytecode.getAssembler();
        Pool pool = bytecode.getConstantPool();

        assembler.emitShort((short) access); // Access
        assembler.emitShort((short) pool.newUTF(name)); // Name
        assembler.emitShort((short) pool.newUTF(desc)); // Descriptor
        assembler.emitShort((short) (attr != null ? 1 : 0)); // Attributes
        attr.toData(assembler);
    }
}
