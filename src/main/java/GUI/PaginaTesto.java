package GUI;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.text.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaginaTesto {
    private final JFrame frameChiamante;
    public JFrame frame;
    public Controller controller;
    private JPanel panel1;
    private JPanel barraSUperiorePanel;
    private JButton Entrabutton;
    private JButton PrecedenteButton;
    private JLabel TitoloPaginaLabel;
    private JPanel TitoloPanel;
    private JTextPane panelTesto;
    private JPanel infoAutorePanel;
    private JLabel nomeAutoreLabel;
    private JLabel dataCreazioneLabel;
    private JButton modificaButton;
    private JButton Versionebutton;
    public String locale = "it_IT";

    public PaginaTesto(Controller controller, JFrame frameC) {
        panelTesto.setText("");
        frameChiamante = frameC;
        this.frame = new JFrame("Pagina");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        if (controller.loggato()) {
            if (!controller.getLoginLoggato().equals(controller.getLoginAutorePaginaSelezionata())) {
                Versionebutton.setVisible(false);
            }
        }

        nomeAutoreLabel.setText(controller.getNomeAutore() + " " + controller.getCognomeAutore());
        dataCreazioneLabel.setText(this.$$$getMessageFromBundle$$$(locale, "dataPublicazione") + ": " + controller.getDataOraCreazionepaginaSelezionata().getYear());

        TitoloPaginaLabel.setText(controller.getTitoloPaginaSelezionata());

        // Aggiungi il JTextPane al frame
        frame.setContentPane(panel1);

        // Imposta il testo nel JTextPane
        ArrayList<String> testoPagina = controller.getTestoPagina();
        String testo = "";
        for (String f : testoPagina) {
            testo = testo + " " + f;
        }
        setFormattedText(testo);

        // Imposta il JTextPane come non editabile
        panelTesto.setEditable(false);

        // Aggiungi il MouseListener per gestire i clic
        panelTesto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int offset = panelTesto.viewToModel(e.getPoint());
                String clickedSentence = getClickedSentence(panelTesto.getText(), offset);

                if (clickedSentence != null) {
                    // Esegui un'azione diversa in base alla frase cliccata
                    handleClickedSentence(clickedSentence);
                }
            }
        });

        PrecedenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });

        if (controller.loggato()) {
            modificaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    ModificaTesto modificaTesto = new ModificaTesto(controller, frame); // da modificare il costruttore
                    modificaTesto.frame.setVisible(true);
                }
            });
        } else {
            modificaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Errori errori = new Errori("Loggarsi prima di modificare");
                    errori.frame.setVisible(true);
                }
            });
        }

        if (controller.loggato()) {
            controller.addPaginaVisualizzata();
            Entrabutton.setText(controller.getLoginLoggato());

            Entrabutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    if (controller.verificaRuoloUtente() == 1) {
                        StoricoUtente storicoUtente = new StoricoUtente(controller, frame);
                        frame.setVisible(false);
                        storicoUtente.frame.setVisible(true);
                    } else {
                        StoricoAutore storicoAutore = new StoricoAutore(controller, frame);
                        frame.setVisible(false);
                        storicoAutore.frame.setVisible(true);
                    }
                }
            });

        } else {
            Entrabutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Login login = new Login(controller, frame, "paginaTesto");
                    frame.setVisible(false);
                    login.frame.setVisible(true);
                }
            });
        }
    }

    private String getClickedSentence(String text, int offset) {
        Pattern pattern = Pattern.compile("\\b[^.!?]*[.!?]");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            if (offset >= start && offset <= end) {
                return text.substring(start, end);
            }
        }

        return null;
    }

    private void handleClickedSentence(String sentence) {
        // Esegui un'azione diversa in base alla frase cliccata
        JOptionPane.showMessageDialog(null, "Frase cliccata: " + sentence);
    }

    private void setFormattedText(String text) {
        StyledDocument doc = panelTesto.getStyledDocument();
        SimpleAttributeSet defaultAttrs = new SimpleAttributeSet();

        try {
            doc.insertString(0, text, defaultAttrs);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("PaginaTesto Example");
            new PaginaTesto(new Controller(), frame);
        });
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        barraSUperiorePanel = new JPanel();
        barraSUperiorePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel1.add(barraSUperiorePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        Entrabutton = new JButton();
        this.$$$loadButtonText$$$(Entrabutton, this.$$$getMessageFromBundle$$$("it_IT", "entra"));
        barraSUperiorePanel.add(Entrabutton);
        PrecedenteButton = new JButton();
        this.$$$loadButtonText$$$(PrecedenteButton, this.$$$getMessageFromBundle$$$("it_IT", "paginaPrecedente"));
        barraSUperiorePanel.add(PrecedenteButton);
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        TitoloPanel = new JPanel();
        TitoloPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(TitoloPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        TitoloPaginaLabel = new JLabel();
        TitoloPaginaLabel.setText("Label");
        TitoloPanel.add(TitoloPaginaLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelTesto = new JTextPane();
        panelTesto.setEditable(false);
        panelTesto.setVerifyInputWhenFocusTarget(true);
        panelTesto.setVisible(true);
        panel1.add(panelTesto, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        infoAutorePanel = new JPanel();
        infoAutorePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(infoAutorePanel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nomeAutoreLabel = new JLabel();
        nomeAutoreLabel.setText("Label");
        infoAutorePanel.add(nomeAutoreLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        infoAutorePanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        dataCreazioneLabel = new JLabel();
        dataCreazioneLabel.setText("Label");
        infoAutorePanel.add(dataCreazioneLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Versionebutton = new JButton();
        this.$$$loadButtonText$$$(Versionebutton, this.$$$getMessageFromBundle$$$("it_IT", "Versione Precedente"));
        infoAutorePanel.add(Versionebutton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modificaButton = new JButton();
        this.$$$loadButtonText$$$(modificaButton, this.$$$getMessageFromBundle$$$("it_IT", "modifica"));
        infoAutorePanel.add(modificaButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
