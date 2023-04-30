import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.awt.*;

public class VideoPlayer {

    private JFrame frame;
    private JTextArea textArea;
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private MediaPlayer mediaPlayer;

    public VideoPlayer(String videoFile) {
        frame = new JFrame("Video Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(200, 600));
        frame.getContentPane().add(scrollPane, BorderLayout.WEST);

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        frame.getContentPane().add(mediaPlayerComponent, BorderLayout.CENTER);

        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        stopButton = new JButton("Stop");

        JPanel controlPanel = new JPanel();
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);
        frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);

        // Setup media player actions
        mediaPlayer = mediaPlayerComponent.mediaPlayer();
        playButton.addActionListener(e -> mediaPlayer.controls().start());
        pauseButton.addActionListener(e -> mediaPlayer.controls().pause());
        stopButton.addActionListener(e -> mediaPlayer.controls().stop());

        mediaPlayer.media().prepare(videoFile);
    }

    public void display() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Please provide a video file as a command line argument.");
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            new MediaPlayerFactory(); // This line will attempt to load the LibVLC library
            VideoPlayer player = new VideoPlayer(args[0]);
            player.display();
        });
    }
}
