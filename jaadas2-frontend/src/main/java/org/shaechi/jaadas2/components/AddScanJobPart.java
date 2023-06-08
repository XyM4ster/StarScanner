package org.shaechi.jaadas2.components;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.IOUtils;
import org.shaechi.jaadas2.entity.User;
import org.shaechi.jaadas2.services.AddNewScanService;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AddScanJobPart extends HorizontalLayout {

    private String uploadPath = "/tmp/";
    AddNewScanService service;
    Long projectId;

    public AddScanJobPart(AddNewScanService service, Long projectId, User user) {
        this.projectId = projectId;
        this.service = service;
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        Div output = new Div();

        upload.addSucceededListener(event -> {
            checkUpload(event.getMIMEType(), event.getFileName(), buffer.getInputStream(event.getFileName()), user);
            UI.getCurrent().getPage().reload();
        });
        upload.addFileRejectedListener(event -> {
            Paragraph component = new Paragraph();
            showOutput(event.getErrorMessage(), component, output);
        });

        add(upload, output);
    }

    private void createErrorDialog(String str)
    {
        Dialog dialog = new Dialog();
        Div content = new Div();
        content.addClassName("my-style");

        content.setText(str);
        dialog.add(content);

// @formatter:off
        String styles = ".my-style { "
                + "  color: red;"
                + " }";
// @formatter:on

        /*
         * The code below register the style file dynamically. Normally you
         * use @StyleSheet annotation for the component class. This way is
         * chosen just to show the style file source code.
         */
        StreamRegistration resource = UI.getCurrent().getSession()
                .getResourceRegistry()
                .registerResource(new StreamResource("styles.css", () -> {
                    byte[] bytes = styles.getBytes(StandardCharsets.UTF_8);
                    return new ByteArrayInputStream(bytes);
                }));
        UI.getCurrent().getPage().addStyleSheet(
                "base://" + resource.getResourceUri().toString());

        dialog.setWidth("400px");
        dialog.setHeight("150px");
        dialog.open();
    }
    private void checkUpload(String mimeType, String fileName,
                                      InputStream stream, User user) {

        if (!fileName.endsWith(".apk")) {
            createErrorDialog("Only APK input is accepted");
            return;
        }
        else if (fileName.contains("..")) {
            createErrorDialog("Only valid filename is accepted, no ..");
            return;
        }
        else {
            try {
                byte[] bytes = IOUtils.toByteArray(stream);
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(bytes,0,bytes.length);
                BigInteger i = new BigInteger(1,md.digest());
                String md5hash = String.format("%1$032X", i);
                FileOutputStream outputStream = new FileOutputStream(uploadPath + File.separator + md5hash + "-" +  fileName);
                outputStream.write(bytes);
                outputStream.close();


                service.addNewApkJobToProject(projectId, md5hash, uploadPath + File.separator + md5hash + "-" +  fileName, user);
            } catch (IOException | NoSuchAlgorithmException e) {

                e.printStackTrace();
                createErrorDialog("Exception: " + e.getMessage());
            }
        }
    }

    private Component createTextComponent(InputStream stream) {
        String text;
        try {
            text = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            text = "exception reading stream";
        }
        return new Text(text);
    }

    private void showOutput(String text, Component content,
                            HasComponents outputContainer) {
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText(text);
        outputContainer.add(p);
        outputContainer.add(content);
    }
}
