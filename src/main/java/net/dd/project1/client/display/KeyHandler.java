package net.dd.project1.client.display;

@FunctionalInterface
public interface KeyHandler {
  void onInput(int keyCode, int action);
}
