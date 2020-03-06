package com.solan.localfdfs.controller;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.domain.upload.FastFile;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.GenerateStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import com.solan.localfdfs.infrastructure.ResultBean;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.tags.form.TextareaTag;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: hyl
 * @date: 2020/3/5 16:22
 */
@RestController
@RequestMapping("/fdfs")
public class FdfsController {
    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private TrackerClient trackerClient;
    //    @Autowired
//    private GenerateStorageClient generateStorageClient;
    @Autowired
    private AppendFileStorageClient appendFileStorageClient;

    @GetMapping("/index")
    public String index() {
        this.trackerClient.listGroups().forEach(x -> System.out.println(x.getGroupName()));
//        this.generateStorageClient.uploadFile()
//        this.appendFileStorageClient.appendFile();
        return "index";
    }

    @PostMapping("/upload")
    public ResultBean<StorePath> upload() throws FileNotFoundException {
        ResultBean<StorePath> result = new ResultBean<>();
        File file = new File("local-fdfs\\file\\新框架说明.pdf");
        String fileName = file.getName();
        try (InputStream inStream = new FileInputStream(file)) {
            FastFile.Builder fastFileBuilder = new FastFile.Builder();
            fastFileBuilder.withFile(inStream, file.length(), fileName.substring(fileName.lastIndexOf(".") + 1));
            StorePath storePath = storageClient.uploadFile(fastFileBuilder.build());
            result.setData(storePath);
            result.setCode(200);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/down")
    public ResultBean<StorePath> down(String group, String path) throws FileNotFoundException {
        ResultBean<StorePath> result = new ResultBean<>();
        File file = new File("local-fdfs\\file\\新框架说明.pdf");
        String fileName = file.getName();
        try (InputStream inStream = new FileInputStream(file)) {
            FastFile.Builder fastFileBuilder = new FastFile.Builder();
            fastFileBuilder.withFile(inStream, file.length(), fileName.substring(fileName.lastIndexOf(".") + 1));
            StorePath storePath = storageClient.downloadFile(group, path, new DownloadCallback<StorePath>() {
                @Override
                public StorePath recv(InputStream ins) throws IOException {
                    return null;
                }
            });
            result.setData(storePath);
            result.setCode(200);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/uploadImg")
    public ResultBean<StorePath> uploadImg() throws FileNotFoundException {
        ResultBean<StorePath> result = new ResultBean<>();
        File file = new File("local-fdfs\\file\\Capture001.png");
        System.out.println(file.getAbsolutePath());
        System.out.println(file.exists());
        String fileName = file.getName();
        String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
        try (InputStream inStream = new FileInputStream(file)) {
            BufferedImage bufferedImage = ImageIO.read(inStream);
            int x = bufferedImage.getWidth() / 2;
            int y = bufferedImage.getHeight() / 2;
            Graphics g = bufferedImage.getGraphics();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String txt = sdf.format(new Date());
            AttributedString ats = new AttributedString(txt);
            Font f = new Font("微软雅黑", Font.BOLD, 20);
            ats.addAttribute(TextAttribute.FONT, f);
            g.drawString(ats.getIterator(), x, y);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, extName, os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            FastFile.Builder fastFileBuilder = new FastFile.Builder();
            fastFileBuilder.withFile(input, file.length(), extName);
            Set<MetaData> metaDataSet = new HashSet<>();
            metaDataSet.add(new MetaData("createdTime", txt));
            StorePath storePath = storageClient.uploadImageAndCrtThumbImage(input, os.size(), extName, metaDataSet);
            Set<MetaData> set = storageClient.getMetadata(storePath.getGroup(), storePath.getPath());
            if (set.iterator().hasNext()) {
                result.getExtras().put("createdTime", set.iterator().next().getValue());
            }
            result.setData(storePath);
            result.setCode(200);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
