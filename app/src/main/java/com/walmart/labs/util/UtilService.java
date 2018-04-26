package com.walmart.labs.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UtilService {
  /**
   * This util method is used for getting all the names of provided enum class.
   *
   * @param e input enum class you want to get all names
   * @return array of string of all the names in enum class
   */
  public String[] getEnumNameList(Class<? extends Enum<?>> e) {
    return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
  }

  /**
   * This util method is used for converting a list of Long type to a comma separated String with
   * all the elements
   *
   * @param listOfLongType a list of Long type
   * @return a comma separated String with all the elements in the original Long list.
   */
  public String listOfLongTypeToCommaSeparatedString(List<Long> listOfLongType) {
    List<String> listOfStringType =
        listOfLongType.stream().map(Object::toString).collect(Collectors.toList());
    return String.join(",", listOfStringType);
  }
}
