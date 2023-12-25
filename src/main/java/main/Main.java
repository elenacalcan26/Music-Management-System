package main;

import exceptions.NoItemPresentInTable;
import parser.JsonParser;

public class Main {
  public static void main(String[] args) throws NoItemPresentInTable {
    JsonParser.parseInput(args[0]);
  }
}
