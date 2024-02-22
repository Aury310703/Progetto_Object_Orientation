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

public class StoricoUtente {
    private JPanel panel;
    private JPanel panelSu;
    private JButton paginaPrecedenteButton;
    private JLabel titoloLabel;
    private JTable tablePagineVisualizzate;
    private JTable tableModProposte;
    private JScrollPane Scrolliniziale;
    private JPanel InseptionPanel;
    private JPanel PanelSinistra;
    private JPanel PanelDestra;
    private JScrollPane ScrollSinistra;
    private JScrollPane ScrollDestra;
    private JPanel infoPanel;
    private JPanel nomePanel;
    private JLabel nomeLabel;
    private JLabel nomeLabel2;
    private JPanel cognomePanel;
    private JLabel cognomeLabel;
    private JLabel cognomeLabel2;
    private JPanel nomeUtentepanel;
    private JLabel nomeUtenteLabel;
    private JLabel nomeUtenteLabel2;
    private JLabel nLabel;
    private JLabel nuLabel;

    public JFrame frame;
    private JFrame frameChiamante;
    private Controller controller;

    public StoricoUtente(Controller controller, JFrame frameChiamante, String locale) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;
        this.frame = new JFrame("WIKI");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        ArrayList<String> pagineVisualizzate = controller.storicoPagineVisualizzate();

        nomeLabel2.setText(controller.getNomeUtenteLoggato());
        cognomeLabel2.setText(controller.getCognomeUtenteLoggato());
        nomeUtenteLabel2.setText(controller.getLoginLoggato());


        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Pagine Visualizzate"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePagineVisualizzate.setModel(model);
        tablePagineVisualizzate.setRowHeight(50);
        for (String pagina : pagineVisualizzate) {
            model.addRow(new Object[]{pagina});
        }
        tablePagineVisualizzate.addMouseListener((new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = tablePagineVisualizzate.getSelectedRow();
                    if (selectedRow != -1) {
                        Object cellValue = tablePagineVisualizzate.getValueAt(selectedRow, 0);
                        String titolo = cellValue.toString();
                        System.out.println(titolo);
                        controller.setPaginaVisualizzata(selectedRow);

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

        ArrayList<String> pagineModificate = controller.getModificate();

        DefaultTableModel modelDestra = new DefaultTableModel(new Object[][]{}, new String[]{"Modifiche Proposte"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModProposte.setModel(modelDestra);
        tableModProposte.setRowHeight(50);

        for (String pagine : pagineModificate) {
            modelDestra.addRow(new Object[]{pagine});
        }
        tableModProposte.addMouseListener((new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = tableModProposte.getSelectedRow();
                    if (selectedRow != -1) {
                        Object cellValue = tableModProposte.getValueAt(selectedRow, 0);
                        String titolo = cellValue.toString();
                        System.out.println(titolo);
                        controller.setPaginaModificata(selectedRow);
                        PaginaTesto paginaTesto = new PaginaTesto(controller, frame, locale);
                        DettagliModifiche dettagliModifiche = new DettagliModifiche(controller, frame, locale);
                        dettagliModifiche.frame.setLocationRelativeTo(frame);
                        dettagliModifiche.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        dettagliModifiche.frame.setVisible(true);
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
                frameChiamante.setVisible(true);
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
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelSu = new JPanel();
        panelSu.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panelSu, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        paginaPrecedenteButton = new JButton();
        this.$$$loadButtonText$$$(paginaPrecedenteButton, this.$$$getMessageFromBundle$$$("it_IT", "paginaPrecedente"));
        panelSu.add(paginaPrecedenteButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        titoloLabel = new JLabel();
        this.$$$loadLabelText$$$(titoloLabel, this.$$$getMessageFromBundle$$$("it_IT", "Storico"));
        panelSu.add(titoloLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Scrolliniziale = new JScrollPane();
        panel.add(Scrolliniziale, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        InseptionPanel = new JPanel();
        InseptionPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        Scrolliniziale.setViewportView(InseptionPanel);
        PanelSinistra = new JPanel();
        PanelSinistra.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        InseptionPanel.add(PanelSinistra, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ScrollSinistra = new JScrollPane();
        PanelSinistra.add(ScrollSinistra, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tablePagineVisualizzate = new JTable();
        ScrollSinistra.setViewportView(tablePagineVisualizzate);
        PanelDestra = new JPanel();
        PanelDestra.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        InseptionPanel.add(PanelDestra, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ScrollDestra = new JScrollPane();
        PanelDestra.add(ScrollDestra, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableModProposte = new JTable();
        ScrollDestra.setViewportView(tableModProposte);
        infoPanel = new JPanel();
        infoPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(infoPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nomePanel = new JPanel();
        nomePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        infoPanel.add(nomePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nomeLabel = new JLabel();
        this.$$$loadLabelText$$$(nomeLabel, this.$$$getMessageFromBundle$$$("it_IT", "nome"));
        nomePanel.add(nomeLabel);
        nomeLabel2 = new JLabel();
        nomeLabel2.setText("Label");
        nomePanel.add(nomeLabel2);
        cognomePanel = new JPanel();
        cognomePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        infoPanel.add(cognomePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cognomeLabel = new JLabel();
        this.$$$loadLabelText$$$(cognomeLabel, this.$$$getMessageFromBundle$$$("it_IT", "cognome"));
        cognomePanel.add(cognomeLabel);
        cognomeLabel2 = new JLabel();
        cognomeLabel2.setText("Label");
        cognomePanel.add(cognomeLabel2);
        nomeUtentepanel = new JPanel();
        nomeUtentepanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        infoPanel.add(nomeUtentepanel, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nomeUtenteLabel = new JLabel();
        this.$$$loadLabelText$$$(nomeUtenteLabel, this.$$$getMessageFromBundle$$$("it_IT", "Username"));
        nomeUtentepanel.add(nomeUtenteLabel);
        nomeUtenteLabel2 = new JLabel();
        nomeUtenteLabel2.setText("Label");
        nomeUtentepanel.add(nomeUtenteLabel2);
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
