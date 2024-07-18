package com.example.ntd.util;

import java.math.BigDecimal;

public class BigDecimalValidator {

  public static boolean isValidBigDecimal(String str) {
    if (str == null) {
      return false;
    }
    try {
      new BigDecimal(str);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }
}