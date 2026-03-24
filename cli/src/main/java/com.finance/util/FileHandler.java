package com.finance.util;

import com.finance.model.Category;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public List<Transaction> readFile(String fileName) {

        try (
                FileReader filereader = new FileReader(fileName);
                CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
        ) {

            List<String[]> allData = csvReader.readAll();
            List<Transaction> transactions = new ArrayList<>();

            allData.forEach(rawTransaction -> {
                Transaction transaction = new Transaction(
                        rawTransaction[0],
                        rawTransaction[1],
                        Double.parseDouble(rawTransaction[2]),
                        TransactionType.getType(rawTransaction[3]),
                        Category.getCategory(rawTransaction[4]),
                        LocalDate.parse(rawTransaction[5])
                );
                transactions.add(transaction);
            });

        return transactions;

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Empty content!");
        return new ArrayList<>();
    }

    public void writeFile(List<Transaction> transactions, String header) {
        File csvOutputFile = new File("./transactions.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println(header);
            transactions.stream()
                    .map(Transaction::createCSVLine)
                    .forEach(pw::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}