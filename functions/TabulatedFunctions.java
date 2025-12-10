package functions;

import java.io.*;

public class TabulatedFunctions {
    private TabulatedFunctions() {}

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть больше 2");
        }
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Выход за границы определения функции");
        }
        FunctionPoint[] array = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            array[i] = new FunctionPoint(leftX + step * i, function.getFunctionValue(leftX + step * i));
        }
        return new ArrayTabulatedFunction(array);
    }
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        int pointCount = dataIn.readInt();
        FunctionPoint[] array = new FunctionPoint[pointCount];
        for (int i = 0; i < pointCount; i++) {
            array[i] = new FunctionPoint(dataIn.readDouble(), dataIn.readDouble());
        }
        return new ArrayTabulatedFunction(array);
    }
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        out.write(String.valueOf(function.getPointsCount()));
        out.write(" ");
        for (int i = 0; i < function.getPointsCount(); i++) {
            out.write(String.valueOf(function.getPointX(i)));
            out.write(" ");
            out.write(String.valueOf(function.getPointY(i)));
            if (i < function.getPointsCount() - 1) {
                out.write(" ");
            }
        }
    }
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.parseNumbers();
        tokenizer.nextToken();
        int pointCount = (int)tokenizer.nval;
        FunctionPoint[] array = new FunctionPoint[pointCount];
        double tempX;
        double tempY;
        for (int i = 0; i < pointCount; i++) {
            tokenizer.nextToken();
            tempX = tokenizer.nval;
            tokenizer.nextToken();
            tempY = tokenizer.nval;
            array[i] = new FunctionPoint(tempX, tempY);
        }
        return new ArrayTabulatedFunction(array);
    }
}
