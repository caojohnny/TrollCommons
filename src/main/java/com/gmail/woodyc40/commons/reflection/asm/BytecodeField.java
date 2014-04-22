package com.gmail.woodyc40.commons.reflection.asm;

import com.gmail.woodyc40.commons.reflection.asm.sun.ClassFileAssembler;

public class BytecodeField {
    private final int    access;
    private final String name;
    private final String desc;

    public BytecodeField(String name, int access, Class<?> type) {
        this.access = access;
        this.name = name;
        this.desc = Descriptors.descField(type);
    }

    public void add(ClassBytecode bytecode) {
        ClassFileAssembler assembler = bytecode.getAssembler();
        Pool pool = bytecode.getConstantPool();

        assembler.emitShort((short) access); // Access
        assembler.emitShort((short) pool.newUTF(name)); // Name
        assembler.emitShort((short) pool.newUTF(desc)); // Descriptor
        assembler.emitShort((short) 0); // Attributes // TODO attributes for code ? fields = value
    }
}
