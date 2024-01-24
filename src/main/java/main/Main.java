package main;

import parser.JsonParser;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    JsonParser.parseInput(args[0]);
  }
}
