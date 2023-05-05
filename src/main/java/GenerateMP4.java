import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;
import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.scale.RgbToYuv420j;


public class GenerateMP4 {
    public static void main(String args[]) throws Exception {
        File file = new File("/Users/paul/IdeaProjects/Project/src/InputVideo.rgb");
        SeekableByteChannel out = NIOUtils.writableFileChannel("/tmp/output.mp4");
        AWTSequenceEncoder encoder = new AWTSequenceEncoder(out, Rational.R(30, 1));
        int width = 480; // width of the video frames
        int height = 270; // height of the video frames
        int fps = 30; // frames per second of the video

        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(width * height * 3);
            buffer.clear();
            int curr = channel.read(buffer);
            buffer.rewind();
            while (curr != -1) {
           //     System.out.println(curr);
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int r = buffer.get() & 0xff;
                        int g = buffer.get() & 0xff;
                        int b = buffer.get() & 0xff;
                        int rgb = (r << 16) | (g << 8) | b;
                        image.setRGB(x, y, rgb);
                    }
                }
                encoder.encodeImage(image);
                buffer.clear();
                channel.read(buffer);
                buffer.rewind();
                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            channel.close();
            raf.close();
            encoder.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //   RgbToYuv420j transform = new RgbToYuv420j();
//  MP4Muxer muxer = new MP4Muxer()

    }
}
