package com.zchess.engine;

public class BishopValidator {

    public static boolean isValid(int fr,int fc,int tr,int tc){

            return Math.abs(fr-tr) == Math.abs(fc-tc);

                }

                }