package kernels;

import java.util.*;

public class FeatureSelectorParams extends CompositeKernelParams {

    long bitmask;
    int numFields;
    
    public FeatureSelectorParams(long fieldBitmask, int numFields,
            double alpha, double s, double b, double d, double g) {
        
        super(alpha, s, b, d, g);
        
        if (bitmask < 0) throw new RuntimeException("ll");
        this.bitmask = fieldBitmask;
        this.numFields = numFields;
   }

    public FeatureSelectorParams(long fieldBitmask, int numFields, CompositeKernelParams K)
    {
        super(K);
        
        if (bitmask < 0) throw new RuntimeException("ll");
        this.bitmask = fieldBitmask;
        this.numFields = numFields;
    }
    
    /**
     * @param addition
     * @param alpha
     * @param s
     * @param b
     * @param d
     * @param g
     */
    public FeatureSelectorParams(long fieldBitmask, int numFields,
            boolean addition, double alpha, double s, double b, double d, double g) {
        super(addition, alpha, s, b, d, g);

        if (bitmask < 0) throw new RuntimeException("ll");
        
        this.bitmask = fieldBitmask;
        this.numFields = numFields;
        
    }

    /**
     * @param addition
     * @param alpha
     * @param s
     * @param b
     * @param d
     * @param g
     * @param c
     */
    public FeatureSelectorParams(long fieldBitmask, int numFields, 
            boolean addition, double alpha, double s, double b, double d, double g, double c) {
        super(addition, alpha, s, b, d, g, c);
        
        if (bitmask < 0) throw new RuntimeException("ll");
        this.bitmask = fieldBitmask;
        this.numFields = numFields;
    }

    /**
     * @param params
     */
    public FeatureSelectorParams(long fieldBitmask, int numFields, double[] params) {
        super(params);
        
        if (bitmask < 0) throw new RuntimeException("ll");
        this.bitmask = fieldBitmask;
        this.numFields = numFields;
    }

    /**
     * @param addition
     * @param params
     */
    public FeatureSelectorParams(long fieldBitmask, int numFields, 
            boolean addition, double[] params) {
        super(addition, params);

        this.bitmask = fieldBitmask;
        this.numFields = numFields;
    }

    public Kernel getKernel( )
    {
        return (Kernel) new FieldSelectKernel(bitmask, numFields,super.getKernel());
    }

    public String toString( )
    {
        String parent = super.toString();
        String fields[] = parent.split("#");
        
        StringBuffer S = new StringBuffer();
        S.append("FS(");
        S.append(Long.toString(bitmask));
        S.append("L,");
        if (fields.length>=1) 
            S.append(fields[0]);
        S.append(")");
        if (fields.length==2) {
            S.append("#");
            S.append(fields[1]);
        }
        
        return S.toString();
    }
    
    public LinkedList expand( )
    {
        LinkedList kernelChildren = super.expand();
        
        LinkedList children = new LinkedList( );
        for (int i = 0; i < numFields; i++)
        {
            
          
            // flip one bit in the field set
            long newmask = newFieldSet(i);
            
            System.err.println("STATUS: bit: " + i + " " + bitmask + " " +newmask);
            
            // create a child for each kernel param child
            Iterator I = kernelChildren.iterator();
            while (I.hasNext()) {
                
                CompositeKernelParams K = (CompositeKernelParams) I.next();
                children.add( new FeatureSelectorParams(newmask,numFields,K.getValues()));
            }
        }
        
        return children;
        
    }
    
    private boolean isSet(int bit)
    {
        long v = 1;
        
        v = 1 << (bit);
        
        return  ((this.bitmask & v) > 0); 
    }
    
 
    private long newFieldSet(int bit)
    {
        long v = 1;
        long newField;
        
        v = 1 << (bit);
        
        newField = bitmask;
        newField = newField ^ v;
        
        return newField;
    }
    
    public static FeatureSelectorParams randomStart(int numFields )
    {
        CompositeKernelParams K = CompositeKernelParams.randomStart();
        
        long value = (long) Math.pow(2,(numFields+1));
        value = value - 1;
        long mask = (long) ( Math.random() * (double)value);
        
        return new FeatureSelectorParams((long)value,numFields,K); 
    }
   
    
    
    public boolean isEqual(KernelParams K2)
    {
        if (!(K2 instanceof FeatureSelectorParams))
            return false;
    
        FeatureSelectorParams FK = (FeatureSelectorParams) K2;
        
        return ((this.numFields == FK.numFields) && (this.bitmask == FK.bitmask) &&
                (super.isEqual(K2)));
    }
    
}
