package com.ultreon.mods.exitconfirmation.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.intellij.lang.annotations.Language;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.objectweb.asm.Opcodes.*;

public class ShutdownTransformer implements IClassTransformer {

    public static final @Language("jvm-class-name") String MINECRAFT_CLASS = "net.minecraft.client.Minecraft";

    @Override
    public byte[] transform(String name, @Language("jvm-class-name") String transformedName, byte[] basicClass) {
        if (!MINECRAFT_CLASS.equals(transformedName)) return basicClass;

        ClassNode classNode = new ClassNode();
        ClassReader reader = new ClassReader(basicClass);
        reader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if ((method.name.equals("shutdown") || method.name.equals("func_71400_g") || method.name.equals("k"))
                    && method.desc.equals("()V")) {
                System.out.println("[ShutdownPatch] Patching Minecraft#shutdown: " + name + " " + method.name + method.desc);

                System.out.println("==== Minecraft.shutdown() Method Dump ====");
                AbstractInsnNode cur;
                for (cur = method.instructions.getFirst(); cur != null; cur = cur.getNext()) {
                    System.out.println(insnToString(cur));
                }

                System.out.println("==== End of Dump ====");


                InsnList insn = new InsnList();
                insn.add(new MethodInsnNode(
                        INVOKESTATIC,
                        "com/ultreon/mods/exitconfirmation/core/Hooks",
                        "allowShutdown",
                        "()Z",
                        false
                ));
                LabelNode continueLabel = new LabelNode();
                insn.add(new JumpInsnNode(IFNE, continueLabel));
                insn.add(new InsnNode(RETURN)); // If false, return early
                insn.add(continueLabel);

                method.instructions.insert(insn); // Inject at start
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }


    // Utility: Converts instruction to readable form
    private String insnToString(AbstractInsnNode insn) {
        Printer printer = new Textifier();
        TraceMethodVisitor mp = new TraceMethodVisitor(printer);
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString().trim();
    }
}
