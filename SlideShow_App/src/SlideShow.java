import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SlideShow extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int NUM_SLIDES = 5;
    private JPanel slidePane;
    private JPanel textPane;
    private JPanel buttonPane;
    private CardLayout card;
    private CardLayout cardText;
    private JButton btnPrev;
    private JButton btnNext;
    private JButton randomButton;
    private Slide[] slides;

    private class Slide {
        private String image;
        private String description;

        public Slide(String image, String description) {
            this.image = image;
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public String getDescription() {
            return description;
        }
    }

    public SlideShow() throws HeadlessException {
        slides = new Slide[NUM_SLIDES];
        initComponent();
    }

    private void initComponent() {
        card = new CardLayout();
        cardText = new CardLayout();
        slidePane = new JPanel();
        textPane = new JPanel();
        textPane.setBackground(Color.CYAN);
        textPane.setBounds(5, 470, 790, 50);
        textPane.setVisible(true);
        buttonPane = new JPanel();
        btnPrev = new JButton();
        btnNext = new JButton();
        randomButton = new JButton();

        setSize(800, 600);
        setLocationRelativeTo(null);
        setTitle("Top 5 Wellness/Detox Destinations");
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        slidePane.setLayout(card);
        textPane.setLayout(cardText);

        for (int i = 0; i < NUM_SLIDES; i++) {
            slides[i] = new Slide(getResizeIcon(i), getTextDescription(i));
            slidePane.add(new JLabel(slides[i].getImage()), "card" + (i + 1));
            textPane.add(new JLabel(slides[i].getDescription()), "cardText" + (i + 1));
        }

        getContentPane().add(slidePane, BorderLayout.CENTER);
        getContentPane().add(textPane, BorderLayout.NORTH);

        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnPrev.setText("Previous");
        btnPrev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goPrevious();
            }
        });
        buttonPane.add(btnPrev);

        randomButton.setText("Random");
        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goRandom();
            }
        });
        buttonPane.add(randomButton);

        btnNext.setText("Next");
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goNext();
            }
        });
        buttonPane.add(btnNext);

        getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }

    private void goPrevious() {
        card.previous(slidePane);
        cardText.previous(textPane);
    }

    private void goNext() {
        card.next(slidePane);
        cardText.next(textPane);
    }

    private void goRandom() {
        int randomIndex = (int) Math.floor(Math.random() * NUM_SLIDES);
        card.show(slidePane, "card" + (randomIndex + 1));
        cardText.show(textPane, "cardText" + (randomIndex + 1));
    }

    private String getResizeIcon(int i) {
        String[] images = {
            "/resources/CalaLuna.jpg",
            "/resources/Kamalaya.jpg",
            "/resources/Aro Ha.jpg",
            "/resources/Six Senses.jpg",
            "/resources/Anada.jpg"
        };
        return "<html><body><img width='800' height='500' src='" + getClass().getResource(images[i]) + "'</body></html>";
    }

    private String getTextDescription(int i) {
        String[] descriptions = {
            "<html><body><font size='5'>#1 Cala Luna Boutique, Costa Rica.</font> <br>Wellness experience in tropical paradise. Transformative yoga experience..</body></html>",
            "<html><body><font size='5'>#2 Kamalaya, Thailand.</font><br>Top wellness retreat in Asia, famous for its beautiful scenery and holistic treatments..</br></body></html>",
            "<html><body><font size='5'>#3 Aro Ha, New Zealand.</font><br>Intimate eco friendly retreat, built to motivate and vitalize the mind and body..</body></html>",
            "<html><body><font size='5'>#4 Six Senses, Portugal.</font><br>Retreat located on a vineyard. Provides 4 different wellness programs to choose from..</br></body></html>",
            "<html><body><font size='5'>#5 Ananda in the Himalayas, India.</font><br>Located in the foothills of the Himalayas. A spiritual retreat offering 80 spa treatments and 10 wellness programs..</br></body></html>"
        };
        return descriptions[i];
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            SlideShow ss = new SlideShow();
            ss.setVisible(true);
        });
    }
}
