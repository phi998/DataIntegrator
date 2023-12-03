import os


class FilePrinter:

    def print_table_to_csv(self, table_name, table_content):
        base_file_name = f"output/{table_name}"
        counter = 1

        while True:
            file_path = f"{base_file_name}_{counter}.csv"
            if not os.path.exists(file_path):
                break
            counter += 1

        with open(file_path, 'w') as file:
            file.write(table_content)


