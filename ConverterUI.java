import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

public class ConverterUI extends JFrame {

    private static final String[] CURRENCIES = {"USD", "EUR", "INR", "GBP", "JPY", "CAD", "AUD"};
    private static final Map<String, String> CURRENCY_SYMBOLS = createCurrencySymbols();

    private static final Color BACKGROUND = new Color(244, 247, 251);
    private static final Color CARD = Color.WHITE;
    private static final Color ACCENT = new Color(28, 90, 168);
    private static final Color ACCENT_DARK = new Color(18, 69, 128);
    private static final Color TEXT = new Color(30, 36, 47);
    private static final Color MUTED = new Color(96, 106, 122);
    private static final Color SOFT_BLUE = new Color(233, 241, 252);

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font RESULT_FONT = new Font("Segoe UI", Font.BOLD, 22);

    private final JTextField amountField;
    private final JComboBox<String> baseCurrencyBox;
    private final JComboBox<String> targetCurrencyBox;
    private final JLabel resultLabel;
    private final RateFetcher rateFetcher;

    public ConverterUI() {
        super("Currency Converter • ₹");

        rateFetcher = new RateFetcher();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 520));
        setSize(760, 520);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ACCENT);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(26, 28, 26, 28));

        JPanel headerTextPanel = new JPanel(new BorderLayout(0, 6));
        headerTextPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Currency Converter");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Simple, readable, live exchange conversions with a clean layout");
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(new Color(230, 238, 250));

        headerTextPanel.add(titleLabel, BorderLayout.NORTH);
        headerTextPanel.add(subtitleLabel, BorderLayout.SOUTH);
        headerPanel.add(headerTextPanel, BorderLayout.WEST);

        JLabel symbolBadge = new JLabel("₹", JLabel.CENTER);
        symbolBadge.setFont(new Font("Segoe UI", Font.BOLD, 34));
        symbolBadge.setOpaque(true);
        symbolBadge.setBackground(new Color(255, 255, 255, 35));
        symbolBadge.setForeground(Color.WHITE);
        symbolBadge.setPreferredSize(new Dimension(68, 68));
        headerPanel.add(symbolBadge, BorderLayout.EAST);

        JPanel bodyPanel = new JPanel(new GridBagLayout());
        bodyPanel.setBackground(BACKGROUND);
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(CARD);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(223, 229, 236)),
                BorderFactory.createEmptyBorder(26, 26, 22, 26)));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;

        JLabel amountLabel = new JLabel("Amount");
        amountLabel.setFont(LABEL_FONT);
        amountLabel.setForeground(TEXT);
        amountField = new JTextField();
        amountField.setFont(INPUT_FONT);
        amountField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(198, 208, 220)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        JLabel baseLabel = new JLabel("Base Currency");
        baseLabel.setFont(LABEL_FONT);
        baseLabel.setForeground(TEXT);
        baseCurrencyBox = new JComboBox<>(CURRENCIES);
        baseCurrencyBox.setFont(INPUT_FONT);

        JLabel targetLabel = new JLabel("Target Currency");
        targetLabel.setFont(LABEL_FONT);
        targetLabel.setForeground(TEXT);
        targetCurrencyBox = new JComboBox<>(CURRENCIES);
        targetCurrencyBox.setFont(INPUT_FONT);
        targetCurrencyBox.setSelectedItem("INR");

        JButton convertButton = new JButton("Convert Now");
        convertButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        convertButton.setBackground(ACCENT);
        convertButton.setForeground(Color.WHITE);
        convertButton.setFocusPainted(false);
        convertButton.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(SOFT_BLUE);
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(199, 216, 240)),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));

        resultLabel = new JLabel("Converted Amount: -");
        resultLabel.setFont(RESULT_FONT);
        resultLabel.setForeground(ACCENT_DARK);
        resultPanel.add(resultLabel, BorderLayout.CENTER);

        JLabel hintLabel = new JLabel("Tip: type a number, pick currencies, and click convert.");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hintLabel.setForeground(MUTED);

        constraints.gridx = 0;
        constraints.gridy = 0;
        cardPanel.add(amountLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        cardPanel.add(amountField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        cardPanel.add(baseLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        cardPanel.add(baseCurrencyBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        cardPanel.add(targetLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        cardPanel.add(targetCurrencyBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        cardPanel.add(convertButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        cardPanel.add(resultPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        cardPanel.add(hintLabel, constraints);

        GridBagConstraints bodyConstraints = new GridBagConstraints();
        bodyConstraints.gridx = 0;
        bodyConstraints.gridy = 0;
        bodyConstraints.weightx = 1.0;
        bodyConstraints.fill = GridBagConstraints.HORIZONTAL;
        bodyPanel.add(cardPanel, bodyConstraints);

        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(bodyPanel, BorderLayout.CENTER);
        add(rootPanel, BorderLayout.CENTER);

        convertButton.addActionListener(event -> convertCurrency());
    }

    private void convertCurrency() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            String baseCurrency = (String) baseCurrencyBox.getSelectedItem();
            String targetCurrency = (String) targetCurrencyBox.getSelectedItem();

            double convertedAmount = rateFetcher.convertAmount(amount, baseCurrency, targetCurrency);
            if (Double.isNaN(convertedAmount)) {
                resultLabel.setText("Converted Amount: service unavailable");
                return;
            }

            resultLabel.setText("Converted Amount: " + getCurrencySymbol(targetCurrency) + String.format(" %.2f", convertedAmount));
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getCurrencySymbol(String currencyCode) {
        return CURRENCY_SYMBOLS.getOrDefault(currencyCode, currencyCode);
    }

    private static Map<String, String> createCurrencySymbols() {
        Map<String, String> symbols = new HashMap<>();
        symbols.put("USD", "$");
        symbols.put("EUR", "€");
        symbols.put("INR", "₹");
        symbols.put("GBP", "£");
        symbols.put("JPY", "¥");
        symbols.put("CAD", "C$");
        symbols.put("AUD", "A$");
        return symbols;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // keep the app running with the default look if the system theme is unavailable
        }

        SwingUtilities.invokeLater(() -> new ConverterUI().setVisible(true));
    }
}
