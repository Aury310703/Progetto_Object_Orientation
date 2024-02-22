package GUI;

import controller.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
    private JButton aggiungiCollegamentiButton;
    private JButton visualizzaCollegamentiButton;
    private JPanel dataPanel;
    private JComboBox giornoComboBox;
    private JComboBox meseComboBox;
    private JComboBox annoComboBox;
    private JButton okVersioneButton;
    private JButton VersioneCorrentButton;
    public String locale = "it_IT";

    public PaginaTesto(Controller controller, JFrame frameC, String locale) {
        panelTesto.setText("");
        frameChiamante = frameC;
        this.frame = new JFrame("Pagina");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        giornoComboBox.setVisible(false);
        meseComboBox.setVisible(false);
        annoComboBox.setVisible(false);
        okVersioneButton.setVisible(false);
        VersioneCorrentButton.setVisible(false);

        if (!controller.loggato() || !controller.getLoginLoggato().equals(controller.getLoginAutorePaginaSelezionata())) {
            Versionebutton.setVisible(false);
            aggiungiCollegamentiButton.setVisible(false);

        }

        int annoInizio = controller.getAnnoInzio();

        for (int i = 1; i <= 31; i++) {
            giornoComboBox.addItem(String.valueOf(i));
        }

        String[] mesi = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};

        for (int i = 0; i < 12; i++) {
            meseComboBox.addItem(mesi[i]);
        }

        if (annoInizio < LocalDate.now().getYear()) {
            for (int i = annoInizio; i <= LocalDate.now().getYear(); i++) {
                annoComboBox.addItem(String.valueOf(i));
            }
        } else if (annoInizio == LocalDate.now().getYear()) {
            annoComboBox.addItem((String.valueOf(annoInizio)));
        }

        nomeAutoreLabel.setText(controller.getNomeAutore() + " " + controller.getCognomeAutore());
        dataCreazioneLabel.setText(this.$$$getMessageFromBundle$$$(locale, "dataPublicazione") + ": " + controller.getDataOraCreazionepaginaSelezionata().getYear());

        TitoloPaginaLabel.setText(controller.getTitoloPaginaSelezionata());

        ArrayList<String> testoPagina = new ArrayList<>();
        String testo = "";
        testoPagina = controller.getTestoPagina();
        for (String f : testoPagina) {
            testo = testo + " " + f;
        }

        panelTesto.setText(testo);
        panelTesto.setEditable(false);

        PrecedenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
                controller.controllaPaginaPrecedenteSalvata();
                controller.setVersionePrecedenteFalse();
                controller.getTestoPagina();
            }
        });

        if (controller.loggato()) {
            modificaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    ModificaTesto modificaTesto = new ModificaTesto(controller, frame, locale);
                    modificaTesto.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    modificaTesto.frame.setVisible(true);
                }
            });
        } else {
            modificaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Errori errori = new Errori("Loggarsi prima di modificare", locale);
                    errori.frame.setLocationRelativeTo(frame);
                    errori.frame.setSize(300, 100);
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
                        StoricoUtente storicoUtente = new StoricoUtente(controller, frame, locale);
                        storicoUtente.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        frame.setVisible(false);
                        storicoUtente.frame.setVisible(true);
                    } else {
                        StoricoAutore storicoAutore = new StoricoAutore(controller, frame, locale);
                        storicoAutore.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        frame.setVisible(false);
                        storicoAutore.frame.setVisible(true);
                    }
                }
            });

        } else {
            Entrabutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Login login = new Login(controller, frame, "paginaTesto", locale);
                    frame.setVisible(false);
                    login.frame.setLocationRelativeTo(frame);
                    login.frame.setResizable(false);
                    login.frame.setSize(400, 200);
                    login.frame.setVisible(true);
                }
            });
        }

        visualizzaCollegamentiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.controllaCollegamenti()) {
                    VisionaCollegamenti visionaCollegamenti = new VisionaCollegamenti(controller, frame, locale);
                    visionaCollegamenti.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    visionaCollegamenti.frame.setVisible(true);
                    frame.setVisible(false);
                } else {
                    Errori errori = new Errori("NON CI SONO COLLEGAMENTI", locale);
                    errori.frame.setLocationRelativeTo(frame);
                    errori.frame.setSize(300, 100);
                    errori.frame.setVisible(true);
                }
            }
        });

        aggiungiCollegamentiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelezionaFraseCollegamento selezionaFraseCollegamento = new SelezionaFraseCollegamento(controller, frame, locale);
                selezionaFraseCollegamento.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                selezionaFraseCollegamento.frame.setVisible(true);
                frame.setVisible(false);
            }
        });

        Versionebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Versionebutton.setVisible(false);
                giornoComboBox.setVisible(true);
                meseComboBox.setVisible(true);
                annoComboBox.setVisible(true);
                okVersioneButton.setVisible(true);
                VersioneCorrentButton.setVisible(true);
            }
        });

        okVersioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int giornoSelezionato = Integer.parseInt((String) giornoComboBox.getSelectedItem());
                int meseSelezionato = 1;
                for (int i = 0; i < mesi.length; i++) {
                    if (mesi[i].equals((String) meseComboBox.getSelectedItem())) {
                        meseSelezionato = i + 1;
                    }
                }
                int annoSelezionato = Integer.parseInt((String) annoComboBox.getSelectedItem());
                LocalDate dataSelezionata = LocalDate.of(annoSelezionato, meseSelezionato, giornoSelezionato);

                controller.setVersionePrecedenteTrue(dataSelezionata);

                ArrayList<String> testoPagina = new ArrayList<>();
                String testo = "";
                testoPagina = controller.getTestoPagina();
                for (String f : testoPagina) {
                    testo = testo + " " + f;
                }
                panelTesto.setText(testo);
            }
        });

        VersioneCorrentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setVersionePrecedenteFalse();
                ArrayList<String> testoPagina = new ArrayList<>();
                String testo = "";
                testoPagina = controller.getTestoPagina();
                for (String f : testoPagina) {
                    testo = testo + " " + f;
                }
                panelTesto.setText(testo);

                Versionebutton.setVisible(true);
                giornoComboBox.setVisible(false);
                meseComboBox.setVisible(false);
                annoComboBox.setVisible(false);
                okVersioneButton.setVisible(false);
                VersioneCorrentButton.setVisible(false);

            }
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
        infoAutorePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(10, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(infoAutorePanel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nomeAutoreLabel = new JLabel();
        nomeAutoreLabel.setText("Label");
        infoAutorePanel.add(nomeAutoreLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        infoAutorePanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        dataCreazioneLabel = new JLabel();
        dataCreazioneLabel.setText("Label");
        infoAutorePanel.add(dataCreazioneLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modificaButton = new JButton();
        this.$$$loadButtonText$$$(modificaButton, this.$$$getMessageFromBundle$$$("it_IT", "modifica"));
        infoAutorePanel.add(modificaButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        aggiungiCollegamentiButton = new JButton();
        this.$$$loadButtonText$$$(aggiungiCollegamentiButton, this.$$$getMessageFromBundle$$$("it_IT", "Aggiungi collegamenti"));
        infoAutorePanel.add(aggiungiCollegamentiButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        visualizzaCollegamentiButton = new JButton();
        this.$$$loadButtonText$$$(visualizzaCollegamentiButton, this.$$$getMessageFromBundle$$$("it_IT", "Visualizza Collegamenti"));
        infoAutorePanel.add(visualizzaCollegamentiButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Versionebutton = new JButton();
        this.$$$loadButtonText$$$(Versionebutton, this.$$$getMessageFromBundle$$$("it_IT", "Versione Precedente"));
        infoAutorePanel.add(Versionebutton, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dataPanel = new JPanel();
        dataPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        infoAutorePanel.add(dataPanel, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        giornoComboBox = new JComboBox();
        dataPanel.add(giornoComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        meseComboBox = new JComboBox();
        dataPanel.add(meseComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        annoComboBox = new JComboBox();
        dataPanel.add(annoComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        okVersioneButton = new JButton();
        this.$$$loadButtonText$$$(okVersioneButton, this.$$$getMessageFromBundle$$$("it_IT", "Visualizza"));
        infoAutorePanel.add(okVersioneButton, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        VersioneCorrentButton = new JButton();
        this.$$$loadButtonText$$$(VersioneCorrentButton, this.$$$getMessageFromBundle$$$("it_IT", "Versione Corrente"));
        infoAutorePanel.add(VersioneCorrentButton, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
