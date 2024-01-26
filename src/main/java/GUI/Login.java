package GUI;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ResourceBundle;


/**
 * The type Login.
 */
public class Login {

    private final JFrame frameChiamante;
    /**
     * The Frame.
     */
    public JFrame frame;
    private JPanel panel;
    private JTextField nomeUtenteField;
    private JPasswordField passwordField;
    private JButton entraButton;
    private JLabel passwordLabel;
    private JLabel nomeUtenteLabel;
    private JButton dietroButton;
    private JButton registratiButton;
    private JLabel ErroreLoginLabel;
    /**
     * The Controller.
     */
    public Controller controller;
    /**
     * The Locale.
     */
    String locale = "it_IT";

    /**
     * Instantiates a new Login.
     *
     * @param controller        the controller
     * @param frameChiamante    the frame chiamante
     * @param controllo         the controllo
     * @param paginaSelezionata the pagina selezionata
     */
    public Login(Controller controller, JFrame frameChiamante, String controllo) {
        this.frameChiamante = frameChiamante;
        this.frame = new JFrame("Login");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();


        dietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                // frame.dispose();

            }
        });

        entraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = nomeUtenteField.getText();
                String password = passwordField.getText();
               controller.verificaLoggato(login, password);
                if (utenteLoggato == null && (!login.isEmpty() || !password.isEmpty())) {
                    ErroreLoginLabel.setText("nomeutente o password errati, riprovare");
                } else if (login.isEmpty()) {
                    ErroreLoginLabel.setText("inserire nomeutente");
                } else if (password.isEmpty()) {
                    ErroreLoginLabel.setText("inserire password");
                }

                if (utenteLoggato != null) {
                    if (utenteLoggato instanceof Autore) {
                        Autore autoreLoggato = (Autore) utenteLoggato;
                        boolean notifiche = controller.controllaNotifiche(autoreLoggato);
                        if (notifiche) {
                            Errori errori = new Errori("hai delle notifiche");
                            errori.frame.setVisible(true);
                        }
                    }
                    if (controllo.equals("Home")) {
                        HomeLoggato homeLoggato = new HomeLoggato(controller, frame, utenteLoggato);
                        homeLoggato.frame.setVisible(true);
                        frame.setVisible(false);
                    } else if (controllo.equals("paginaTesto")) {
                        PaginaTesto paginaTesto = new PaginaTesto(controller, frame, paginaSelezionata, utenteLoggato);
                        frame.setVisible(false);
                        paginaTesto.frame.setVisible(true);
                    }
                }
            }
        });
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registrazione registazione = new Registrazione(controller, frame);
                frame.setVisible(false);
                registazione.frame.setVisible(true);
                //9frame.dispose();
            }
        });
    }

    /**
     * Instantiates a new Login.
     *
     * @param controller     the controller
     * @param frameChiamante the frame chiamante
     * @param controllo      the controllo
     * @param titolo         the titolo
     */
    public Login(Controller controller, JFrame frameChiamante, String controllo, String titolo) {
        this.frameChiamante = frameChiamante;
        this.frame = new JFrame("Login");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        dietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                // frame.dispose();

            }
        });

        entraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = nomeUtenteField.getText();
                String password = passwordField.getText();
                Utente utenteLoggato = controller.verificaLoggato(login, password);
                if (utenteLoggato == null && (!login.isEmpty() || !password.isEmpty())) {
                    ErroreLoginLabel.setText("nomeutente o password errati, riprovare");
                } else if (login.isEmpty()) {
                    ErroreLoginLabel.setText("inserire nomeutente");
                } else if (password.isEmpty()) {
                    ErroreLoginLabel.setText("inserire password");
                }

                if (utenteLoggato != null) {
                    if (utenteLoggato instanceof Autore) {
                        Autore autoreLoggato = (Autore) utenteLoggato;
                        boolean notifiche = controller.controllaNotifiche(autoreLoggato);
                        if (notifiche) {
                            Errori errori = new Errori("hai delle notifiche");
                            errori.frame.setVisible(true);
                        }
                    }

                    if (controllo.equals("listaTitoli")) {
                        ListaTitoli listaTitoli = new ListaTitoli(controller, frame, titolo, utenteLoggato);
                        frame.setVisible(false);
                        listaTitoli.frame.setVisible(true);
                    } else if (controllo.equals("Home")) {
                        HomeLoggato homeLoggato = new HomeLoggato(controller, frame, utenteLoggato);
                        homeLoggato.frame.setVisible(true);
                        frame.setVisible(false);
                    }
                }
            }
        });
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registrazione registazione = new Registrazione(controller, frame);
                frame.setVisible(false);
                registazione.frame.setVisible(true);
                //9frame.dispose();
            }
        });
    }

    public Login(Controller controller, JFrame frameChiamante, String controllo) {
        this.frameChiamante = frameChiamante;
        this.frame = new JFrame("Login");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();


        dietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                // frame.dispose();

            }
        });

        entraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = nomeUtenteField.getText();
                String password = passwordField.getText();
                Utente utenteLoggato = controller.verificaLoggato(login, password);
                if (utenteLoggato == null && (!login.isEmpty() || !password.isEmpty())) {
                    ErroreLoginLabel.setText("nomeutente o password errati, riprovare");
                } else if (login.isEmpty()) {
                    ErroreLoginLabel.setText("inserire nomeutente");
                } else if (password.isEmpty()) {
                    ErroreLoginLabel.setText("inserire password");
                }

                if (utenteLoggato != null) {
                    if (utenteLoggato instanceof Autore) {
                        Autore autoreLoggato = (Autore) utenteLoggato;
                        boolean notifiche = controller.controllaNotifiche(autoreLoggato);
                        if (notifiche) {
                            Errori errori = new Errori("hai delle notifiche");
                            errori.frame.setVisible(true);
                        }
                    }
                    HomeLoggato homeLoggato = new HomeLoggato(controller, frame, utenteLoggato);
                    homeLoggato.frame.setVisible(true);
                    frame.setVisible(false);
                }
            }
        });
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registrazione registazione = new Registrazione(controller, frame);
                frame.setVisible(false);
                registazione.frame.setVisible(true);
                //9frame.dispose();
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
        panel = new JPanel();
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        passwordLabel = new JLabel();
        this.$$$loadLabelText$$$(passwordLabel, this.$$$getMessageFromBundle$$$("it_IT", "password"));
        panel1.add(passwordLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField = new JPasswordField();
        panel1.add(passwordField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nomeUtenteLabel = new JLabel();
        this.$$$loadLabelText$$$(nomeUtenteLabel, this.$$$getMessageFromBundle$$$("it_IT", "Username"));
        panel2.add(nomeUtenteLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nomeUtenteField = new JTextField();
        panel2.add(nomeUtenteField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        entraButton = new JButton();
        this.$$$loadButtonText$$$(entraButton, this.$$$getMessageFromBundle$$$("it_IT", "entra"));
        panel.add(entraButton, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dietroButton = new JButton();
        this.$$$loadButtonText$$$(dietroButton, this.$$$getMessageFromBundle$$$("it_IT", "paginaPrecedente"));
        panel.add(dietroButton, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registratiButton = new JButton();
        this.$$$loadButtonText$$$(registratiButton, this.$$$getMessageFromBundle$$$("it_IT", "Registrati"));
        panel.add(registratiButton, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ErroreLoginLabel = new JLabel();
        ErroreLoginLabel.setForeground(new Color(-1766656));
        ErroreLoginLabel.setText("");
        panel.add(ErroreLoginLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
    private void $$$loadLabelText$$$(JLabel component, String text) {
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
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
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
        return panel;
    }



}
