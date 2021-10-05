/*
 * Copyright (C) 2014 Delcio Amarillo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.javadeepcafe.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DateFormatter;
import com.javadeepcafe.domain.Fabricante;
import com.javadeepcafe.domain.Procesador;
import com.javadeepcafe.tablemodel.GenericDomainTableModel;

/**
 * @author Delcio Amarillo
 */
public class Demo {
    
    private GenericDomainTableModel<Procesador> procesadoresTableModel;
    private Fabricante fabricanteSeleccionado;
    
    private Action nuevoProcesadorAction, eliminarProcesadorAction;
    
    private void createAndShowGui() {        
        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        content.add(getTopPanel(), BorderLayout.NORTH);
        content.add(getCenterPanel(), BorderLayout.CENTER);
        
        JFrame frame = new JFrame("Bienvenido!");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(content);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private JPanel getTopPanel() {        
        
        final DefaultComboBoxModel<Fabricante> comboBoxModel = new DefaultComboBoxModel<>();
        JComboBox comboBox = new JComboBox(comboBoxModel);
        comboBox.setPrototypeDisplayValue("Por favor seleccione un fabricante");
        
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Fabricante) {
                    Fabricante fabricante = (Fabricante)value;
                    setText(fabricante.getNombre());
                }
                return this;
            }
        });
        
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fabricanteSeleccionado = (Fabricante)comboBoxModel.getSelectedItem();
                procesadoresTableModel.clearTableModelData();
                procesadoresTableModel.addRows(fabricanteSeleccionado.getProcesadores());
                nuevoProcesadorAction.setEnabled(fabricanteSeleccionado != null);
            }
        });
        
        JButton button = new JButton("+");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel labelNombre = new JLabel("Nombre:");
                labelNombre.setHorizontalAlignment(JLabel.RIGHT);
                JTextField textField = new JTextField(20);
                
                JLabel labelFechaFundacion = new JLabel("Fecha fundación:");
                labelFechaFundacion.setHorizontalAlignment(JLabel.RIGHT);
                
                JSpinner spinner = new JSpinner(new SpinnerDateModel());
                JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
                
                DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
                formatter.setAllowsInvalid(false);
                formatter.setOverwriteMode(true);
                
                spinner.setEditor(editor);
                
                JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
                panel.add(labelNombre);
                panel.add(textField);
                panel.add(labelFechaFundacion);
                panel.add(spinner);
                
                int opcion = JOptionPane.showConfirmDialog(null, panel, "Nuevo Fabricante"
                                                        , JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (opcion == JOptionPane.OK_OPTION) {
                    String nombre = textField.getText().trim();
                    Date fechaFundacion = (Date)spinner.getValue();
                    comboBoxModel.addElement(new Fabricante(nombre, fechaFundacion));
                } 
            }
        });
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        topPanel.setBorder(new TitledBorder("Fabricantes:"));
        topPanel.add(new JLabel("Seleccione un fabricante:"));
        topPanel.add(comboBox);
        topPanel.add(button);
        
        return topPanel;
    }
    
    private JPanel getCenterPanel() {        
        Object[] columnIdentifiers = new Object[]{
            "Denominación", 
            "Núcleos", 
            "Frecuencia CPU @GHz", 
            "Cache MB"
        };
        
        procesadoresTableModel = new GenericDomainTableModel<Procesador>(Arrays.asList(columnIdentifiers)) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch(columnIndex) {
                    case 0: return String.class;
                    case 1: return Integer.class;
                    case 2: return Double.class;
                    case 3: return Double.class;
                }
                throw new ArrayIndexOutOfBoundsException(columnIndex);
            }
            
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Procesador procesador = getDomainObject(rowIndex);
                switch(columnIndex) {
                    case 0: return procesador.getDenominacion();
                    case 1: return procesador.getNumeroNucleos();
                    case 2: return procesador.getFrecuenciaCpu();
                    case 3: return procesador.getCache();
                        default: throw new ArrayIndexOutOfBoundsException(columnIndex);
                }
            }
            
            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                Procesador procesador = getDomainObject(rowIndex);
                switch(columnIndex) {
                    case 0: procesador.setDenominacion((String)aValue); break;
                    case 1: procesador.setNumeroNucleos((Integer)aValue); break;
                    case 2: procesador.setFrecuenciaCpu((Double)aValue); break;
                    case 3: procesador.setCache((Double)aValue); break;
                        default: throw new ArrayIndexOutOfBoundsException(columnIndex);
                }
                notifyTableCellUpdated(rowIndex, columnIndex);
            }
        };
        
        final JTable table = new JTable(procesadoresTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 300));
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        eliminarProcesadorAction = new AbstractAction("-") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = table.getSelectedRows();
                for (int row = selectedRows.length - 1; row >= 0; row--) {
                    int viewIndex = selectedRows[row];
                    int modelIndex = table.convertRowIndexToModel(viewIndex);
                    fabricanteSeleccionado.removeProcesador(procesadoresTableModel.getDomainObject(viewIndex));
                    procesadoresTableModel.deleteRow(modelIndex);
                }
                setEnabled(procesadoresTableModel.getRowCount() > 0);
            }
        };        
        eliminarProcesadorAction.setEnabled(false);
        
        nuevoProcesadorAction = new AbstractAction("+") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Procesador p = new Procesador(fabricanteSeleccionado);
                fabricanteSeleccionado.addProcesador(p);
                procesadoresTableModel.addRow(p);
                eliminarProcesadorAction.setEnabled(true);
            }
        };        
        nuevoProcesadorAction.setEnabled(false);
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(new JButton(nuevoProcesadorAction));
        buttonsPanel.add(new JButton(eliminarProcesadorAction));
        
        JPanel centerPanel = new JPanel(new BorderLayout(8,8));
        CompoundBorder border = BorderFactory.createCompoundBorder(new TitledBorder("Procesadores:"), new EmptyBorder(8,8,8,8));
        centerPanel.setBorder(border);
        centerPanel.add(new JScrollPane(table));
        centerPanel.add(buttonsPanel, BorderLayout.EAST);
        
        return centerPanel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    new Demo().createAndShowGui();
                } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

} // End of class Demo