package com.chavaillaz.search;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;

public class MultiLineToStringStyle extends MultilineRecursiveToStringStyle {

    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor.</p>
     * <p>
     * <p>Use the static constant rather than instantiating.</p>
     */
    public MultiLineToStringStyle() {
        super();
        this.setUseShortClassName(true);
        this.setUseIdentityHashCode(false);
    }

}
