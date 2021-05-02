/*
 * Created on Sep 20, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kernels;

import java.util.LinkedList;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface KernelParams {
    public abstract Kernel getKernel();

    public abstract String toString();

    public abstract LinkedList expand();

    public abstract boolean isEqual(KernelParams K);
}