package GUI;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Schermata usata per selezionare la pagina da collegare alla frase scelta nella schermata precedente.
 * In questa pagina è possibile cercare delle pagine, viusalizzare le pagine, selezionare la pagina scelta e tornare indietro alla pagina precedente.
 */
public class InserisciCollegamento {
    private JPanel panel;
    private JPanel panel1;
    private JScrollPane checkPanel;
    private JPanel CercaPanel;
    private JTextField titoloField;
    private JButton cercaButton;
    private JPanel precedentePanel;
    private JButton paginaPrecedenteButton;
    private JTable titoliTable;

    public JFrame frame;
    private JFrame frameChiamante;

    /**
     * Istanzia una nuova schermata Inserisci collegamento.
     *
     * @param controller  realizza le operazioni algoritmiche
     * @param frameC      schermata da cui viene istanziata HomeLoggato
     * @param indiceFrase indica la numerazine della frase scelta
     * @param locale      indica la lingua.
     */
    public InserisciCollegamento(Controller controller, JFrame frameC, int indiceFrase, String locale) {
        this.frameChiamante = frameC;
        this.frame = new JFrame("WIKI");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        String titoloSalvato = controller.getTitoloSelezionato();
        String pagineTrovate = this.$$$getMessageFromBundle$$$(locale, "pagineTrovate");
        String scelta = this.$$$getMessageFromBundle$$$(locale, "scelta");
        String seleziona = this.$$$getMessageFromBundle$$$(locale, "seleziona");

        paginaPrecedenteButton.setText(this.$$$getMessageFromBundle$$$(locale, "paginaPrecedente"));
        cercaButton.setText(this.$$$getMessageFromBundle$$$(locale, "cerca"));

        cercaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setTitoloSelezionato(titoloField.getText());
                DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{pagineTrovate, scelta}) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                ArrayList<String> titoli = controller.getTitoliCercati(controller.getTitoloSelezionato());
                titoliTable.setModel(model);
                titoliTable.setRowHeight(50);
                for (String t : titoli) {
                    model.addRow(new Object[]{t, seleziona});

                }
                titoliTable.addMouseListener((new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            int clickedColumn = titoliTable.columnAtPoint(e.getPoint());
                            int clickedRow = titoliTable.rowAtPoint(e.getPoint());

                            if (clickedColumn != -1 && clickedRow != -1) {
                                Object cellValue = titoliTable.getValueAt(clickedRow, clickedColumn);
                                if (!cellValue.equals(seleziona)) {
                                    controller.addPaginaSelezionata(clickedRow);
                                    PaginaTesto paginaTesto = new PaginaTesto(controller, frame, locale);
                                    paginaTesto.frame.setLocationRelativeTo(frame);
                                    paginaTesto.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                                    paginaTesto.frame.setVisible(true);
                                    frame.setVisible(false);
                                } else {
                                    Errori errori = new Errori("inserimentoCollegamento", locale);
                                    errori.frame.setLocationRelativeTo(frame);
                                    errori.frame.setSize(300, 100);
                                    controller.addPaginaCollegata(indiceFrase, clickedRow);
                                    frame.setVisible(false);
                                    frameChiamante.setVisible(true);
                                    errori.frame.setVisible(true);
                                    frame.dispose();
                                    controller.setTitoloSelezionato(titoloSalvato);
                                    controller.getTitoliCercati(controller.getTitoloSelezionato());
                                }
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
            }
        });

        paginaPrecedenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
                controller.setTitoloSelezionato(titoloSalvato);
                controller.getTitoliCercati(controller.getTitoloSelezionato());
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
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        checkPanel = new JScrollPane();
        panel1.add(checkPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        titoliTable = new JTable();
        checkPanel.setViewportView(titoliTable);
        CercaPanel = new JPanel();
        CercaPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(CercaPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        titoloField = new JTextField();
        CercaPanel.add(titoloField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cercaButton = new JButton();
        this.$$$loadButtonText$$$(cercaButton, this.$$$getMessageFromBundle$$$("it_IT", "cerca"));
        CercaPanel.add(cercaButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        precedentePanel = new JPanel();
        precedentePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(precedentePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        paginaPrecedenteButton = new JButton();
        this.$$$loadButtonText$$$(paginaPrecedenteButton, this.$$$getMessageFromBundle$$$("it_IT", "paginaPrecedente"));
        precedentePanel.add(paginaPrecedenteButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
     * $$$ get root component $$$ j component.
     *
     * @return the j component
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
