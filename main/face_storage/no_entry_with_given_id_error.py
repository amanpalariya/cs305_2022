class NoEntryWithGivenIdError(Exception):
    def __init__(self, id: int) -> None:
        super().__init__(f"No record with id {id}")