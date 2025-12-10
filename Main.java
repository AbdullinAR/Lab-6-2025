import functions.*;
import functions.basic.*;
import java.io.*;
import threads.*;
import java.util.Random;

public class Main {

    public static void complicatedThreads() {
        Task task = new Task();
        task.setTasksCount(100);
        Semaphore semaphore = new Semaphore();
        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);
        generator.setPriority(Thread.MIN_PRIORITY);
        integrator.setPriority(Thread.MAX_PRIORITY);
        generator.start();
        integrator.start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {}
        generator.interrupt();
        integrator.interrupt();
    }

    public static void simpleThreads() {
        Task task = new Task();
        task.setTasksCount(100);

        task.setFunction(new Log(2));
        task.setLeft(1);
        task.setRight(2);
        task.setStep(0.1);

        Thread generator = new Thread(new SimpleGenerator(task));
        Thread integrator = new Thread(new SimpleIntegrator(task));

        generator.setPriority(Thread.MIN_PRIORITY);
        integrator.setPriority(Thread.MAX_PRIORITY);

        generator.start();
        integrator.start();
    }

    public static void nonThread() {
        Random rand = new Random();
        Task task = new Task();
        task.setTasksCount(100);
        for (int i = 0; i < task.getTasksCount(); i++) {
            double base = 1 + 9 * rand.nextDouble();
            Function logFunc = new Log(base);
            double left = rand.nextDouble() * 100;
            double right = 100 + rand.nextDouble() * 100;
            double step = rand.nextDouble();
            if (step == 0) step = 0.1;
            task.setFunction(logFunc);
            task.setLeft(left);
            task.setRight(right);
            task.setStep(step);
            System.out.printf("Source %.4f %.4f %.4f%n", task.getLeft(), task.getRight(), task.getStep());
            try {
                double result = Functions.integrate(task.getFunction(), task.getLeft(), task.getRight(), task.getStep());
                System.out.printf("Result %.4f %.4f %.4f %.6f%n", task.getLeft(), task.getRight(), task.getStep(), result);
            } catch (IllegalArgumentException e) {
                System.out.println("Result error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {

        //Машинный эпсилон
        double DEFAULT_EPSILON = 1e-9;

        complicatedThreads();
        //simpleThreads();
        //nonThread();

        /*
        //-------------------------------------------------------------------------------------------------------------
        Function exp = new Exp();
        double exact = Math.E - 1;
        double step = 0.001;
        double value = Functions.integrate(exp, 0, 1, step);
        System.out.println("Численно:" + value);
        System.out.println("Аналитически:" + exact);
        System.out.println("Ошибка:" + Math.abs(value - exact));
        //-------------------------------------------------------------------------------------------------------------
        */
    }
}
