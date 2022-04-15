from types import coroutine
from matplotlib.pyplot import title
from main.libraries.excel_writer.excel_writer import CellFormat, ExcelCoordinate, ExcelWorkbook
from main.table_writer.table_writer import TableWriter
from typing import List


class ExcelTableWriter(TableWriter):
    def __init__(self, header: List[str], filepath: str, sheet_name: str) -> None:
        super().__init__(header)
        self.__excel_workbook = ExcelWorkbook(filepath)
        self.__excel_worksheet = self.__excel_workbook.add_sheet(sheet_name)

        self.__setup_formats()
        self.__setup_counters()
        self.__add_header_to_worksheet()

    def __setup_formats(self):
        self.__header_format = CellFormat(bold=True, bg_color='#EEEEEE')
        self.__row_format = CellFormat()

    def __setup_counters(self):
        self.__current_row_index = 0

    def __add_header_to_worksheet(self):
        self.__add_row_to_worksheet(0, self.get_header(), self.__header_format)
        self.__current_row_index += 1

    def __add_row_to_worksheet(self, row_index, row, format):
        for col, col_content in enumerate(row):
            coord = ExcelCoordinate(row_index, col)
            self.__excel_worksheet.write(coord, col_content, format)

    def add_row(self, row: List[str]):
        super().add_row(row)
        self.__add_row_to_worksheet(self.__current_row_index, row, self.__row_format)
        self.__current_row_index += 1
    
    def get_number_of_rows_added(self) -> int:
        super().get_number_of_rows_added()
        return max(self.__current_row_index - 1, 0)

    def close(self):
        super().close()
        self.__excel_workbook.close()
