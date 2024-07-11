package io.fireflyest.emberlib.config;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * 插件配置测试
 * @author Fireflyest
 * @since 1.0
 */
public class PluginConfigTest {

    @Test
    public void testRead() {
        final ClassReader cr;
        try {
            cr = new ClassReader("io.fireflyest.emberlib.command.AbstractCommand");
            cr.accept(new ClassVisitor(Opcodes.ASM7) {

                public void visit(int version, int access, String name,
                        String signature, String superName, String[] interfaces) {
                    System.out.println(name + " extends " + superName + " {");
                }

                public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                    System.out.println("    " + desc + " " + name);
                    return null;
                }

                public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                        String[] exceptions) {
                    System.out.println("    " + name + desc);
                    return null;
                }

                public void visitEnd() {
                    System.out.println("}");
                }

            }, 0);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void testWrite() {
        // final ClassWriter cw = new ClassWriter(0);
        // cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT +
        // Opcodes.ACC_MODULE,
        // "pkg/Comparable", null, "java/lang/Object",
        // new String[] { "pkg/Mesurable" });

        // cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
        // "LESS", "I",
        // null, -1).visitEnd();

        // cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
        // "EQUAL", "I",
        // null, 0).visitEnd();

        // cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
        // "GREATER", "I",
        // null, 1).visitEnd();

        // cw.visitField(Opcodes.ACC_PUBLIC, "name", "I",
        // null, 1).visitEnd();

        // cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "compareTo",
        // "(Ljava/lang/Object;)I", null, null).visitEnd();

        // cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "mName",
        // "(Ljava/lang/Object;)I", null, null).visitEnd();

        // cw.visitEnd();
        // final byte[] b = cw.toByteArray();

        byte[] b = null;
        try {
            b = new RunnableDump().dump();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (FileOutputStream outputStream = new FileOutputStream(new File("./test.class"))) {
            outputStream.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Class c = new MyClassLoader().defineClass("pkg.Comparable", b);
    }

    @Test
    public void test() {
        byte[] b1 = null;

        try (FileInputStream inputStream = new FileInputStream("./test.class")) {
            b1 = inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ClassWriter cw = new ClassWriter(0);
        // cv forwards all events to cw
        final ClassVisitor cv = new ClassVisitor(Opcodes.ASM4, cw) {

            @Override
            public void visit(int version, int access, String name,
                    String signature, String superName, String[] interfaces) {
                cv.visit(Opcodes.V1_5, access, name, signature, superName, interfaces);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name,
                    String desc, String signature, String[] exceptions) {
                System.out.println(name);
                System.out.println(desc);
                if (name.equals("mName") && desc.equals("(Ljava/lang/Object;)I")) {
                    // do not delegate to next visitor-> this removes the method
                    return null;
                }
                return cv.visitMethod(access, name, desc, signature, exceptions);
            }

        };
        final ClassReader cr = new ClassReader(b1);
        cr.accept(cv, 0);
        final byte[] b2 = cw.toByteArray();

        try (FileOutputStream outputStream = new FileOutputStream(new File("./test.class"))) {
            outputStream.write(b2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testType() {
        System.out.println("getDescriptor = " + Type.getType(String.class).getDescriptor());
        System.out.println("getInternalName = " + Type.getType(String.class).getInternalName());
        for (Type type : Type.getArgumentTypes("(IF)V")) {
            System.out.println("getArgumentTypes = " + type);
        }
        System.out.println("getReturnType = " + Type.getReturnType("(IF)V"));
    }

    public class RunnableDump implements Opcodes {

        public byte[] dump() throws Exception {
            final ClassWriter cw = new ClassWriter(0);
            final FieldVisitor fv;
            final MethodVisitor mv;
            final AnnotationVisitor av0;
            cw.visit(V1_5, ACC_PUBLIC + ACC_MODULE,
            "java/lang/Runnable", null, "java/lang/Object", null);
            
            {
                mv = cw.visitMethod(ACC_PUBLIC, "run", "()I",
                null, null);
                mv.visitCode();
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, "pkg/Bean", "f", "I");
                mv.visitInsn(IRETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }

            cw.visitEnd();
            return cw.toByteArray();
        }
    }

    class MyClassLoader extends ClassLoader {
        public Class defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }

}
