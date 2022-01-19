package gui;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class Avatar {

    public static final String BASE64_PREFIX = "data:image/png;base64,";

    private static byte[] create(int id, String userName) throws IOException {
        int absID = Math.abs(id);
        int width = 20;
        int grid = 5;
        int padding = width / 2;
        int size = width * grid + width;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D _2d = img.createGraphics();
        _2d.setColor(new Color(240, 240, 240));
        _2d.fillRect(0, 0, size, size);
        _2d.setColor(randomColor(80, 200));
        char[] idchars = createIdent(absID);
        int i = idchars.length;
        for (int x = 0; x < Math.ceil(grid / 2.0); x++) {
            for (int y = 0; y < grid; y++) {
                if (idchars[--i] < 53) {
                    _2d.fillRect((padding + x * width), (padding + y * width), width, width);
                    if (x < Math.floor(grid / 2)) {
                        _2d.fillRect((padding + ((grid - 1) - x) * width), (padding + y * width), width, width);
                    }
                }
            }
        }
        _2d.dispose();

//        StringBuilder path = new StringBuilder();
//        path.append("client\\src\\main\\resources\\img\\");
//        path.append(userName);
//        path.append(".png");
//        ImageIO.write(img, "png", new File(path.toString()));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(img, "png", byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }

    private static Color randomColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(Math.abs(bc - fc));
        int g = fc + random.nextInt(Math.abs(bc - fc));
        int b = fc + random.nextInt(Math.abs(bc - fc));
        return new Color(r, g, b);
    }

    private static char[] createIdent(int id) {
        BigInteger bi_content = new BigInteger((id + "").getBytes());
        BigInteger bi = new BigInteger(id + "identicon" + id, 36);
        bi = bi.xor(bi_content);
        return bi.toString(10).toCharArray();
    }

    public static byte[] createAvatar(String userName) {
        byte[] avatar = null;
        try {
            avatar = create(userName.hashCode(), userName); // извлекаем из имени профиля hashCode
        } catch (IOException e) {                                                // на случай, если он получился отрицательным
            e.printStackTrace();                                                   // делаем его по модулю.
        }
        return avatar;
    }
}




