import java.util.Scanner;

public class Main {

    public static double fun(double x) { //значение функции в точке
       return Math.pow(Math.E, 2*x)*Math.sin(x);
    }

    public static double antiderivativeFun(double a, double b) { //значение первообразной функции в точке
        return 0.2*Math.pow(Math.E, 2*b)*(2*Math.sin(b)-Math.cos(b)) - 0.2*Math.pow(Math.E, 2*a)*(2*Math.sin(a)-Math.cos(a));
    }

    public static double round(double value, int places) { //функция для округления (избегаем погрешность сложения вещественных чисел)
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public static void rectangleMethod(double a, double b, int n) { //метод прямоугольников

        double h = (b-a)/n; //шаг
        double[] splitPoints = new double[n+1];
        double[] functionValues = new double[n+1];
        double leftRect = 0.0;
        double rightRect= 0.0;
        double middleRect = 0.0;

        for (int i = 0; i <= n; i++) {
            splitPoints[i] = a + i * h;
            functionValues[i] = fun(splitPoints[i]);
        }

        for (int i = 0; i<splitPoints.length - 1; i++) {
            leftRect +=functionValues[i];
        }
        leftRect *= h;

        for (int i = 1; i<splitPoints.length; i++) {
            rightRect +=functionValues[i];
        }
        rightRect *= h;

        for (int i = 0; i < splitPoints.length - 1; i++) {
            double midPoint = (splitPoints[i] + splitPoints[i+1]) / 2;
            middleRect += fun(midPoint);
        }
        middleRect *= h;

        for (int i = 0; i<splitPoints.length; i++) {
            System.out.println("x"+i +": " + round(splitPoints[i],3) + ", f(x" + i +"): " + round(functionValues[i], 3) + "\n");
        }

        System.out.println("По формуле левых прямоугольников: " + round(leftRect, 3) + " Погрешность: " + round((Math.abs(leftRect - antiderivativeFun(a,b))), 3));
        System.out.println("По формуле правых прямоугольников: " + round(rightRect, 3) + " Погрешность: " + round((Math.abs(rightRect - antiderivativeFun(a,b))), 3));
        System.out.println("По формуле средних прямоугольников: " + round(middleRect, 3) + " Погрешность: " + round((Math.abs(middleRect - antiderivativeFun(a,b))), 3));
        System.out.println("Первообразная: " + round(antiderivativeFun(a,b), 3));
    }

    public static void trapezoidMethod(double a, double b, int n) {

        double h = (b-a)/n; //шаг
        double[] splitPoints = new double[n+1];
        double[] functionValues = new double[n+1];
        double[] partitionValues = new double[n];

        for (int i = 0; i <= n; i++) {
            splitPoints[i] = a + i * h;
            functionValues[i] = fun(splitPoints[i]);
        }

        for (int i = 0; i<splitPoints.length -1; i++) {
            partitionValues[i] = h*(functionValues[i] + functionValues[i+1])/2;
        }

        double result = 0.0;

        for (int i = 0; i< partitionValues.length; i++) {
            result += partitionValues[i];
        }

        System.out.println(round(result, 5) + " Погрешность: " + round((Math.abs(result - antiderivativeFun(a,b))), 5));
        System.out.println("Первообразная: " + round(antiderivativeFun(a,b), 3));
    }

    public static void simpsonMethod(double a, double b, int n) {

        if (n % 2 != 0) {
            System.out.println("Ошибка: количество разбиений n должно быть чётным!");
            return;
        }

        double h = (b-a)/n; //шаг
        double[] splitPoints = new double[n+1];
        double[] functionValues = new double[n+1];

        for (int i = 0; i <= n; i++) {
            splitPoints[i] = a + i * h;
            functionValues[i] = fun(splitPoints[i]);
        }

        double result = functionValues[0] + functionValues[n];

        for (int i = 1; i < splitPoints.length - 1; i++) {
            if (i%2 == 0) {
                result += 2*functionValues[i];
            }

            else {
                result += 4*functionValues[i];
            }
        }

        result *= h / 3.0;

        System.out.println(round(result, 3) + " Погрешность: " + round((Math.abs(result - antiderivativeFun(a,b))), 3));
        System.out.println("Первообразная: " + round(antiderivativeFun(a,b), 3));
    }

    public static void gaussianMethod(double a, double b) {

        double[] legendrePolynomial = {-0.93246951, -0.6612093, -0.23861918, 0.23861918, 0.6612093, 0.93246951};
        double[] weight = {0.171324492, 0.36076157, 0.46793, 0.46793, 0.36076157, 0.171324492};

        double result = 0.0;

        for (int i=0; i<6; i++) {
            double xi = 0.5*(b - a)*legendrePolynomial[i] + 0.5*(b + a);
            result += weight[i] * fun(xi);
        }

        result *= (b-a)/2;
        System.out.println(round(result, 3) + " Погрешность: " + round((Math.abs(result - antiderivativeFun(a,b))), 3));
        System.out.println("Первообразная: " + round(antiderivativeFun(a,b), 3));

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите номер задания (1-4)");
        int task = Integer.parseInt(scanner.nextLine());

        double a = 0;
        double b = Math.PI / 2;

        System.out.println("Введите n - количество разбиений");
        int n = Integer.parseInt(scanner.nextLine());

        switch (task) {
            case 1:
                System.out.println("\tМетод прямоугольников: ");
                rectangleMethod(a, b, n);
                break;

            case 2: {
                System.out.println("\tМетод трапеций: ");
                trapezoidMethod(a, b, n);
                break;
            }

            case 3: {
                System.out.println("\tМетод Симпсона: ");
                simpsonMethod(a, b, n);
                break;
            }

            case 4:
                gaussianMethod(a, b);
                break;

            default:
                System.out.println("Неверный номер (1-4)");
                return;
        }
    }
}