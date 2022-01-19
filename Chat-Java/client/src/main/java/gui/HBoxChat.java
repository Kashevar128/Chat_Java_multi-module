package gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import network.ClientProfile;
import network.Message;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class HBoxChat extends HBox {

    private ImageView photo;
    private Label label;
    private String userName;
    private String stringValue;
    private boolean inOrOut;

    public HBoxChat(Message message) {
        this.userName = message.getProfile().getNameUser();
        this.stringValue = message.getStringValue();
        this.inOrOut = message.isInOrOut();
        this.photo = getFormatIV(decodeByteToImage(message.getProfile().getAvatar()));
        this.label = getDialogLabel(inOrOut, stringValue);

        if (inOrOut) {
            this.setAlignment(Pos.CENTER_LEFT);
            this.getChildren().addAll(photo, label);
        }
        if (!inOrOut) {
            this.setAlignment(Pos.CENTER_RIGHT);
            this.getChildren().addAll(label, photo);
        }
        HBox.setMargin(photo, new Insets(10, 10, 10, 10));
        HBox.setMargin(label, new Insets(20, 10, 20, 10));


    }

    public HBoxChat(ClientProfile clientProfile) {
        this.photo = getFormatIV(decodeByteToImage(clientProfile.getAvatar()));
        this.userName = clientProfile.getNameUser();
        this.label = getListUsersLabel(userName);
        this.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().addAll(photo, label);

        HBox.setMargin(photo, new Insets(10, 10, 10, 10));
        HBox.setMargin(label, new Insets(20, 10, 20, 0));
    }

    Label getListUsersLabel(String name) {
        label = new Label(name);
        label.setFont(new Font("Arial", 16));
        label.setMaxWidth(200);
        label.setWrapText(true);

        return label;
    }

    Label getDialogLabel(boolean inOrOut, String text) {
        String stylesInGoingLabel = "-fx-background-color: #D3EEDF;" +
                "-fx-background-radius: 25px;" + "-fx-background-insets: -10;";

        String stylesOutGoingLabel = "-fx-background-color: #B9D7E9;" + //
                "-fx-background-radius: 25px;" + "-fx-background-insets: -10;";

        Label label = new Label(text);
        if (inOrOut) {
            label.setStyle(stylesInGoingLabel);
        }
        if (!inOrOut) label.setStyle(stylesOutGoingLabel);
        label.setFont(new Font("Arial", 16));
        label.setMaxWidth(450);
        label.setWrapText(true);

        return label;
    }

    public static Image decodeByteToImage(byte[] byteAva) {
        Image imageFX = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(byteAva);
        try {
            BufferedImage image = ImageIO.read(bais);
            imageFX = SwingFXUtils.toFXImage(image, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFX;
    }

    public static ImageView getFormatIV(Image image) {
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        return imageView;
    }
}


