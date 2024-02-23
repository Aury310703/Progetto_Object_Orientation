package GUI;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * In questa schermata verranno mostarti i riusltati in base alla ricerca effettuata nella schermata precedente dall'utente.
 * Verranno dunque mostrati all'utente un elenco con i titoli delle pagine trovate che possono essere successivamente visualizzate, inoltre sarà possibile accedere all'area personale (o loggarsi nel caso in cui l'accesso non è stato effettuato) o tornare alla pagina precedente.
 */
public class ListaTitoli {

    private final JFrame frameChiamante;

    public JFrame frame;
    private JPanel panel;
    private JPanel TitoloPanel;
    private JPanel BottoniPanel;
    private JButton entraButton;
    private JButton paginaPrecedenteButton;
    private JLabel TitoloLabel;
    private JTable TitoliTable;
    private JScrollPane scrollpane;

    /**
     * Istanzia una nuova schermata ListaTitoli.
     *
     * @param controller     realizza le operazioni algoritmiche
     * @param frameC schermata da cui viene istanziata ListaTitoli
     * @param locale         indica la lingua.
     */
    public ListaTitoli(Controller controller, JFrame frameC, String locale) {
        frameChiamante = frameC;
        this.frame = new JFrame("Titoli");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        paginaPrecedenteButton.setText(this.$$$getMessageFromBundle$$$(locale, "paginaPrecedente"));
        entraButton.setText(this.$$$getMessageFromBundle$$$(locale, "entra"));
        TitoloLabel.setText(this.$$$getMessageFromBundle$$$(locale, "TESTI"));

        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{this.$$$getMessageFromBundle$$$(locale, "TESTI")}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ArrayList<String> titoli = controller.getTitoliCercati(controller.getTitoloSelezionato());
        TitoliTable.setModel(model);
        TitoliTable.setRowHeight(50);
        for (String t : titoli) {
            model.addRow(new Object[]{t});

        }
        TitoliTable.addMouseListener((new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = TitoliTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Object cellValue = TitoliTable.getValueAt(selectedRow, 0);
                        String titolo = cellValue.toString();
                        System.out.println(titolo);

                        controller.addPaginaSelezionata(selectedRow);
                        PaginaTesto paginaTesto = new PaginaTesto(controller, frame, locale);
                        paginaTesto.frame.setLocationRelativeTo(frame);
                        paginaTesto.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        paginaTesto.frame.setVisible(true);
                        frame.setVisible(false);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        }));

        paginaPrecedenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                if (controller.loggato()) {
                    HomeLoggato homeLoggato = new HomeLoggato(controller, frame, locale);
                    homeLoggato.frame.setVisible(true);
                } else
                    frameChiamante.setVisible(true);

                //frame.dispose();
            }
        });

        if (controller.loggato()) {
            entraButton.setText(controller.getLoginLoggato());
            entraButton.addActionListener(new ActionListener() {
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
            entraButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Login login = new Login(controller, frame, "listaTitoli", locale);
                    login.frame.setLocationRelativeTo(frame);
                    login.frame.setResizable(false);
                    login.frame.setSize(400, 200);
                    login.frame.setVisible(true);
                    frame.setVisible(false);

                }
            });
        }


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
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        TitoloPanel = new JPanel();
        TitoloPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(TitoloPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        TitoloLabel = new JLabel();
        this.$$$loadLabelText$$$(TitoloLabel, this.$$$getMessageFromBundle$$$("it_IT", "TESTI"));
        TitoloPanel.add(TitoloLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BottoniPanel = new JPanel();
        BottoniPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel.add(BottoniPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        entraButton = new JButton();
        this.$$$loadButtonText$$$(entraButton, this.$$$getMessageFromBundle$$$("it_IT", "LOGIN"));
        BottoniPanel.add(entraButton);
        paginaPrecedenteButton = new JButton();
        this.$$$loadButtonText$$$(paginaPrecedenteButton, this.$$$getMessageFromBundle$$$("it_IT", "paginaPrecedente"));
        BottoniPanel.add(paginaPrecedenteButton);
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollpane = new JScrollPane();
        panel.add(scrollpane, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        TitoliTable = new JTable();
        scrollpane.setViewportView(TitoliTable);
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
     * $$$ get root component $$$ j component.
     *
     * @return the j component
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
