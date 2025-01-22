package models;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int size;
    private char[][] grid;
    private List<Ship> ships;

    // Конструктор
    public Board(int size) {
        this.size = size;
        this.grid = new char[size][size];
        this.ships = new ArrayList<>();
        initializeGrid();
    }

    // Инициализация сетки пустыми клетками
    private void initializeGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = '~'; // '~' обозначает воду
            }
        }
    }

    // Геттеры
    public int getSize() { return size; }
    public char[][] getGrid() { return grid; }
    public List<Ship> getShips() { return ships; }

    // Метод для размещения корабля
    public boolean placeShip(Ship ship) {
        if (!canPlaceShip(ship)) {
            return false;
        }

        int x = ship.getStartX();
        int y = ship.getStartY();
        String orientation = ship.getOrientation();

        for (int i = 0; i < ship.getSize(); i++) {
            if (orientation.equalsIgnoreCase("горизонтальная")) {
                grid[x][y + i] = 'S'; // 'S' обозначает корабль
            } else {
                grid[x + i][y] = 'S';
            }
        }

        ships.add(ship);
        return true;
    }

    // Проверка, можно ли разместить корабль
    public boolean canPlaceShip(Ship ship) {
        int x = ship.getStartX();
        int y = ship.getStartY();
        String orientation = ship.getOrientation();

        // Проверка границ поля
        if (orientation.equalsIgnoreCase("горизонтальная")) {
            if (y + ship.getSize() > size) return false;
        } else {
            if (x + ship.getSize() > size) return false;
        }

        // Проверка пересечений с другими кораблями
        for (int i = 0; i < ship.getSize(); i++) {
            int currentX = x;
            int currentY = y;
            if (orientation.equalsIgnoreCase("горизонтальная")) {
                currentY += i;
            } else {
                currentX += i;
            }
            if (grid[currentX][currentY] != '~') {
                return false;
            }
        }

        return true;
    }

    // Обработка выстрела
    public String receiveShot(int x, int y) {
        if (grid[x][y] == '~') {
            grid[x][y] = 'M'; // 'M' обозначает промах
            return "Промах";
        } else if (grid[x][y] == 'S') {
            grid[x][y] = 'H'; // 'H' обозначает попадание
            for (Ship ship : ships) {
                if (ship.getOrientation().equalsIgnoreCase("горизонтальная")) {
                    if (x == ship.getStartX() && y >= ship.getStartY() && y < ship.getStartY() + ship.getSize()) {
                        ship.hit();
                        if (ship.isSunk()) {
                            return "Потоплен";
                        } else {
                            return "Попадание";
                        }
                    }
                } else {
                    if (y == ship.getStartY() && x >= ship.getStartX() && x < ship.getStartX() + ship.getSize()) {
                        ship.hit();
                        if (ship.isSunk()) {
                            return "Потоплен";
                        } else {
                            return "Попадание";
                        }
                    }
                }
            }
            return "Попадание";
        } else {
            return "Клетка уже была поражена";
        }
    }

    // Отображение собственного поля
    public void displayOwnBoard() {
        System.out.println("Собственное поле:");
        displayGrid(true);
    }

    // Отображение поля противника
    public void displayOpponentBoard() {
        System.out.println("Поле противника:");
        displayGrid(false);
    }

    // Метод для отображения сетки
    private void displayGrid(boolean own) {
        // Вывод заголовка с буквами колонок
        System.out.print("  ");
        for (int j = 0; j < size; j++) {


            System.out.print((char)('A' + j) + " ");
        }
        System.out.println();

        for (int i = 0; i < size; i++) {
            // Вывод номера строки
            if (i < 9) System.out.print((i + 1) + " ");
            else System.out.print((i + 1) + "");

            for (int j = 0; j < size; j++) {
                char cell = grid[i][j];
                if (!own && cell == 'S') {
                    System.out.print("~ ");
                } else {
                    System.out.print(cell + " ");
                }
            }
            System.out.println();
        }
    }
}