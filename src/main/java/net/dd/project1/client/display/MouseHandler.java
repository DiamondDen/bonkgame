package net.dd.project1.client.display;

@FunctionalInterface
public interface MouseHandler {
  void onInput(int button, int action, int mouseX, int mouseY);
}
