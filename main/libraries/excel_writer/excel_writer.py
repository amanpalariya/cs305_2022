import xlsxwriter
from dataclasses import dataclass


class ExcelCoordinate:
    def __init__(self, row, col) -> None:
        """
        row and col are 0 indexed
        """
        self.row = row
        self.col = col


@dataclass
class CellFormat:
    bold: bool = False
    italic: bool = False
    font_color: str = None
    bg_color: str = None


class ExcelSheet:
    def __init__(self, workbook, sheet_name: str) -> None:
        self.__workbook = workbook
        self.__worksheet = self.__workbook.add_worksheet(sheet_name)

    def __get_format(self, cell_format: CellFormat):
        format_dict = {}
        if cell_format.bold:
            format_dict['bold'] = True
        if cell_format.italic:
            format_dict['italic'] = True
        if cell_format.font_color:
            format_dict['font_color'] = cell_format.font_color
        if cell_format.bg_color:
            format_dict['bg_color'] = cell_format.bg_color
        return self.__workbook.add_format(format_dict)

    def write(self, coordinate: ExcelCoordinate, content: str, cell_format: CellFormat):
        format = self.__get_format(cell_format)
        self.__worksheet.write(coordinate.row, coordinate.col, content, format)


class ExcelWorkbook:
    def __init__(self, workbook_filepath):
        self.__workbook = xlsxwriter.Workbook(workbook_filepath)

    def add_sheet(self, sheet_name: str):
        return ExcelSheet(self.__workbook, sheet_name)

    def close(self):
        self.__workbook.close()
