package render;
import javafx.scene.Parent;

public interface IRender<T> {
	public Parent render(T item);
}
