# Описание

Решение задания для возможности попасть на собеседование. Условие:

            1. Реализовать классы Client, Account, Payment для описания бизнесс модели: клиенты, счета клиентов, платежи между клиентами
            2. Реализовать интерфейс
            
            public interface ClientService {
                  double getClientBalance(Client client, List<Account> accounts);
                  Client getClientWithMaxBalance(List<Account> accounts);
            }
            
            3. Предложить структуру таблиц базы данных для хранения информации о клиентах, счетах, платежах
            4. Написать запрос для выборки фамилий клиентов с минимальным и максимальным балансом.
            
Использовались стандарные библиотеки за исключением H2Database и фрейма Maven.

#Использование

        https://github.com/pepsik/SimpleBank.git