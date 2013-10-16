/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc;

//import br.com.danielferber.gittocc.cc.ClearToolActivity;
//import br.com.danielferber.gittocc.cc.ClearToolDriver;
import br.com.danielferber.gittocc.cc.ClearToolCommander;
import br.com.danielferber.gittocc.cc.ClearToolProcessBuilder;
import br.com.danielferber.gittocc.cc.VobUpdater;
import br.com.danielferber.gittocc.git.GitCommander;
import br.com.danielferber.gittocc.git.GitHistory;
import br.com.danielferber.gittocc.git.GitHistoryBuilder;
import br.com.danielferber.gittocc.git.GitProcessBuilder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author X7WS
 */
public class MainFrame extends javax.swing.JFrame {

    Future<?> tarefa;
    final ExecutorService tarefaExecutor = Executors.newCachedThreadPool();
    final DefaultListModel logListModel = new DefaultListModel();

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        SwingAppender.add(new SwingAppender.Handler() {
            Queue<ILoggingEvent> queue = new ConcurrentLinkedQueue<ILoggingEvent>();

            public void handle(final ILoggingEvent e) {
                queue.add(e);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        logListModel.addElement(e);
                    }
                });
            }
        });
        jList1.setCellRenderer(new LogListCellRenderer());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setText("Git working directory:");

        jLabel2.setText("Clear Case UCM view:");

        jTextField1.setText("K:\\git\\jconsuelo-sync");

        jTextField2.setText("K:\\CcViews\\X7WS_JCONSUELO_UCM_DVL_MI00248890_SYNC\\JCONSUELO\\Fontes");

        jButton1.setText("Atualizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Configurações:");

        jLabel4.setText("Executável GIT:");

        jTextField3.setText("C:\\Users\\x7ws\\Programas\\PortableGit\\bin\\git.exe");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel5.setText("Executável ClearTool:");

        jTextField4.setText("C:\\Program Files\\IBM\\RationalSDLC\\ClearCase\\bin\\cleartool.exe");

        jLabel6.setText("Console:");

        jList1.setModel(logListModel);
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        synchronized (this) {
            if (tarefa != null && !tarefa.isDone()) {
                /* A tarefa ainda está executando. */
                tarefa.cancel(true);
            } else {
                final File gitDir = new File(jTextField1.getText());
                final File gitExecutable = new File(jTextField3.getText());
                final File ccDir = new File(jTextField2.getText());
                final File ccExecutable = new File(jTextField4.getText());
                final File ccCommitStampFile = new File(ccDir, "commit.txt");
                final String previousCommit;

                try {
                    if (!gitDir.exists() || !gitDir.isDirectory()) {
                        throw new IOException("O caminho do repositório Git não existe ou não é um diretório.");
                    }
                    if (!gitExecutable.exists() || !gitExecutable.isFile() || !gitExecutable.canExecute()) {
                        throw new IOException("O caminho da ferramenta GIT não existe ou não é arquivo executável.");
                    }
                    if (!ccDir.exists() || !ccDir.isDirectory()) {
                        throw new IOException("O caminho da view Clearcase não existe ou não é um diretório.");
                    }
                    if (!ccExecutable.exists() || !ccExecutable.isFile() || !ccExecutable.canExecute()) {
                        throw new IOException("O caminho da ferramenta ClearTool não existe ou não é arquivo executável.");
                    }
                    if (!ccCommitStampFile.exists() || !ccCommitStampFile.isFile() || !gitExecutable.canRead()) {
                        throw new IOException("O caminho da marca na vob Clearcase não existe ou não é um arquivo válido.");
                    }
                    previousCommit = new Scanner(ccCommitStampFile).next();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Parâmetros inválidos", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                tarefa = tarefaExecutor.submit(new Runnable() {
                    public void run() {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                jButton1.setText("Cancelar");
                            }
                        });
                        try {
                            final GitProcessBuilder gitProcessBuilder = new GitProcessBuilder(gitDir, gitExecutable);
                            final GitHistoryBuilder historyBuilder = new GitHistoryBuilder(gitProcessBuilder, gitDir, previousCommit);
                            final GitHistory gitHistory = historyBuilder.call();
                            final ClearToolProcessBuilder clearToolProcessBuilder = new ClearToolProcessBuilder(ccDir, ccExecutable);
                            final VobUpdater vobUpdater = new VobUpdater(gitHistory, clearToolProcessBuilder, ccDir);
                            vobUpdater.call();
                        } catch (final Exception e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    String mensagem = "Falha ao sincronizar view ClearCase com repositório GIT.\n" + e.getMessage();
                                    Exception ee = e;
                                    while (ee.getCause() != null && ee.getCause() != ee) {
                                        ee = (Exception) ee.getCause();
                                        mensagem += "\n" + ee.getMessage();
                                    }
                                    JOptionPane.showMessageDialog(MainFrame.this, mensagem, "Erro na sincronização", JOptionPane.ERROR_MESSAGE);
                                }
                            });
                        } finally {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    jButton1.setText("Atualizar");
                                }
                            });
                            synchronized (MainFrame.this) {
                                tarefa = null;
                            }
                        }
                    }
                });
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        loadFieldValues();
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        saveFiledValue();
    }//GEN-LAST:event_formWindowClosed

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
//        saveFiledValue();
    }//GEN-LAST:event_formWindowDeactivated

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
//        loadFieldValues();
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        saveFiledValue();
    }//GEN-LAST:event_formWindowClosing
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables

    private void loadFieldValues() {
        File userDir = new File(System.getProperty("user.dir"));
        File propertiesFile = new File(userDir, "gittocc.properties");

        if (!propertiesFile.exists()) {
            return;
        }

        FileInputStream fi = null;
        try {
            fi = new FileInputStream(propertiesFile);

            Properties p = new Properties();
            p.load(fi);
            jTextField1.setText(p.getProperty("git.repository", ""));
            jTextField2.setText(p.getProperty("cc.vob", ""));
            jTextField3.setText(p.getProperty("git.executable", ""));
            jTextField4.setText(p.getProperty("cc.executable", ""));
            fi.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(MainFrame.this, e.getLocalizedMessage(), "Salvar propriedades", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (fi != null) {
                try {
                    fi.close();
                } catch (IOException e) {
                    // ignore;
                }
            }
        }
    }

    private void saveFiledValue() {
        File userDir = new File(System.getProperty("user.dir"));
        File propertiesFile = new File(userDir, "gittocc.properties");

        FileOutputStream of = null;
        try {
            of = new FileOutputStream(propertiesFile);

            Properties p = new Properties();
            p.setProperty("git.repository", jTextField1.getText());
            p.setProperty("cc.vob", jTextField2.getText());
            p.setProperty("git.executable", jTextField3.getText());
            p.setProperty("cc.executable", jTextField4.getText());
            p.store(of, "Git to ClearCase");
            of.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(MainFrame.this, e.getLocalizedMessage(), "Salvar propriedades", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (of != null) {
                try {
                    of.close();
                } catch (IOException e) {
                    // ignore;
                }
            }
        }
    }
}