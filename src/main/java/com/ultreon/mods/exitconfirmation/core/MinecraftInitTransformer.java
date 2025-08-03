package com.ultreon.mods.exitconfirmation.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

public class MinecraftInitTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!transformedName.equals("net.minecraft.client.Minecraft")) return basicClass;

        ClassReader reader = new ClassReader(basicClass);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM5, writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                if (name.equals("<init>")) {
                    return new MethodVisitor(Opcodes.ASM5, mv) {
                        @Override
                        public void visitInsn(int opcode) {
                            if (opcode == Opcodes.RETURN) {
                                super.visitMethodInsn(
                                    Opcodes.INVOKESTATIC,
                                    "com/ultreon/mods/exitconfirmation/ExitConfirmation",
                                    "init",
                                    "()V",
                                    false
                                );
                            }
                            super.visitInsn(opcode);
                        }
                    };
                }
                return mv;
            }
        };

        reader.accept(visitor, 0);
        return writer.toByteArray();
    }
}
