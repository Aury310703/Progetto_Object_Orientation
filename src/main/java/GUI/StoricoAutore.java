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

public class StoricoAutore {
    private JPanel panel;
    private JPanel panel1;
    private JButton paginaPrecedenteButton;
    private JButton notificheButton;
    private JTable modificheProposteTable;
    private JTable pagineCreateTable;
    private JTable pagineVisualizzateTable;
    private JPanel panel2;
    private JScrollPane modificheProposteScrollPane;
    private JScrollPane pagineCreateScrollPane;
    private JPanel panel3;
    private JPanel panel4;
    private JPanel panel5;
    private JScrollPane pagineVisualizzateScrollPane;
    private JPanel panel6;
    private JPanel panel7;
    public JFrame frame;
    private JFrame frameChiamante;
    public Controller controller;

    public StoricoAutore(Controller controller, JFrame frameChiamante, String locale) {
        this.controller = controller;
        this.frameChiamante = frameChiamante;
        this.frame = new JFrame("WIKI");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        String pagineVisualizzateText = this.$$$getMessageFromBundle$$$(locale, "pagineVisualizzate");
        String modificheProposteText = this.$$$getMessageFromBundle$$$(locale, "modificheProposte");
        String pagineCreateText = this.$$$getMessageFromBundle$$$(locale, "pagineCreate");


        notificheButton.setText(this.$$$getMessageFromBundle$$$(locale, "notifiche"));
        paginaPrecedenteButton.setText(this.$$$getMessageFromBundle$$$(locale, "paginaPrecedente"));

        ArrayList<String> pagineVisualizzate = controller.storicoPagineVisualizzate();
        DefaultTableModel modelVisualizzate = new DefaultTableModel(new Object[][]{}, new String[]{pagineVisualizzateText}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pagineVisualizzateTable.setModel(modelVisualizzate);
        pagineVisualizzateTable.setRowHeight(50);
        for (String pagina : pagineVisualizzate) {
            modelVisualizzate.addRow(new Object[]{pagina});
        }
        pagineVisualizzateTable.addMouseListener((new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = pagineVisualizzateTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Object cellValue = pagineVisualizzateTable.getValueAt(selectedRow, 0);
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


        DefaultTableModel modelProposte = new DefaultTableModel(new Object[][]{}, new String[]{modificheProposteText}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modificheProposteTable.setModel(modelProposte);
        modificheProposteTable.setRowHeight(50);
        for (String pagine : pagineModificate) {
            modelProposte.addRow(new Object[]{pagine});
        }
        modificheProposteTable.addMouseListener((new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = modificheProposteTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Object cellValue = modificheProposteTable.getValueAt(selectedRow, 0);
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

        ArrayList<String> pagineCreate = controller.storicoPagineCreate();
        DefaultTableModel modelCreate = new DefaultTableModel(new Object[][]{}, new String[]{pagineCreateText}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pagineCreateTable.setModel(modelCreate);
        pagineCreateTable.setRowHeight(50);
        for (String pagine : pagineCreate) {
            modelCreate.addRow(new Object[]{pagine});
        }
        pagineCreateTable.addMouseListener((new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = pagineCreateTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Object cellValue = pagineCreateTable.getValueAt(selectedRow, 0);
                        String titolo = cellValue.toString();

                        controller.setPaginaCreata(selectedRow);
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

        if (controller.controllaNotifiche()) {
            notificheButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Notifiche notifiche = new Notifiche(controller, frame, locale);
                    frame.setVisible(false);
                    notifiche.frame.setResizable(false);
                    notifiche.frame.setLocation(200, 400);
                    notifiche.frame.setSize(1500, 250);
                    notifiche.frame.setVisible(true);
                }
            });
        } else {
            notificheButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        Errori erorri = new Errori("noNotifiche", locale);
                        erorri.frame.setLocationRelativeTo(frame);
                        erorri.frame.setSize(300, 100);
                        erorri.frame.setVisible(true);
                }
            });
        }
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
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
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        paginaPrecedenteButton = new JButton();
        this.$$$loadButtonText$$$(paginaPrecedenteButton, this.$$$getMessageFromBundle$$$("it_IT", "paginaPrecedente"));
        panel2.add(paginaPrecedenteButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        notificheButton = new JButton();
        this.$$$loadButtonText$$$(notificheButton, this.$$$getMessageFromBundle$$$("it_IT", "notifiche"));
        panel2.add(notificheButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        scrollPane1.setViewportView(panel3);
        panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pagineVisualizzateScrollPane = new JScrollPane();
        panel5.add(pagineVisualizzateScrollPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        pagineVisualizzateTable = new JTable();
        pagineVisualizzateScrollPane.setViewportView(pagineVisualizzateTable);
        panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        modificheProposteScrollPane = new JScrollPane();
        panel6.add(modificheProposteScrollPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        modificheProposteTable = new JTable();
        modificheProposteScrollPane.setViewportView(modificheProposteTable);
        panel7 = new JPanel();
        panel7.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel7, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pagineCreateScrollPane = new JScrollPane();
        panel7.add(pagineCreateScrollPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        pagineCreateTable = new JTable();
        pagineCreateScrollPane.setViewportView(pagineCreateTable);
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
        return panel;
    }

}
