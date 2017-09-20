import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String url = "https://rozetka.com.ua/all-tv/c80037/";
        RozetkaCategoryParser parser = new RozetkaCategoryParser(url);

        parser.process();
    }

}
