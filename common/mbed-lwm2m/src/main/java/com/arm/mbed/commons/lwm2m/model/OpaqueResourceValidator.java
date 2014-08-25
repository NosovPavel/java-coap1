/**
 * Copyright (C) 2011-2014 ARM Limited. All rights reserved.
 */
package com.arm.mbed.commons.lwm2m.model;

/**
 * @author nordav01
 */
public class OpaqueResourceValidator {

    private static class OpaqueValidator implements ResourceValidator {

       @Override
       public boolean isValid(byte[] value) {
           return value != null;
       }

       @Override
       public boolean isValid(int value) {
           int size = 32 - Integer.numberOfLeadingZeros(value);
           size = size%8 == 0 ? size/8 : size/8 + 1;
           return isValid(new byte[size] );
       }

       @Override
       public boolean isValid(String value) {
           return value != null && isValid(value.getBytes() );
       }

    }
    
    private static class OpaqueLengthValidator extends OpaqueValidator {

       private int lengthMin;
       private int lengthMax;
        
       public OpaqueLengthValidator (String range) {
           String[] length = range.split(",");
           if ("".equals(range) ) {
               lengthMin = 0;
               lengthMax = Integer.MAX_VALUE;
           } else if (length.length == 1) {
               lengthMin = lengthMax = Integer.parseInt(length[0]);
           } else if (length.length > 1) {
               lengthMin = Integer.parseInt(length[0]);
               lengthMax = Integer.parseInt(length[1]);
           }
       } 

       @Override
       public boolean isValid(byte[] value) {
           return super.isValid(value) && value.length>=lengthMin && value.length<=lengthMax;
       }
       
    }

    @SuppressWarnings("PMD.SimplifyStartsWith")
    static ResourceValidator createResourceValidator (String range) {
        if (range.startsWith("{") && range.endsWith("}")) {
            return new OpaqueLengthValidator(range.substring(1, range.length()-1));
        }
        
        return new OpaqueValidator();
    
    }
    
}