class MetadataNotFoundError(Exception):
    def __init__(self) -> None:
        super().__init__()