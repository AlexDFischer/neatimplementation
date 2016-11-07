package neatimplementation;
public class Static
{
    public static final int DEFAULT_POP_SIZE = 100;
    public static final double DEFAULT_THRESHOLD_DIFFERENCE = 3.0;
    public static final double DEFAULT_C1 = 1.0, DEFAULT_C2 = 1.0, DEFAULT_C3 = 0.4;
    
    public static double sigmoid(double x)
    {
        return Math.tanh(4.9*x);
    }
}