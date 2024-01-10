

class QualityMeasurer:

    def __init__(self, df_expected, df_result):
        self.df_expected = df_expected
        self.df_result = df_result

    def get_metrics(self, output_df, expected_df):
        quality_dict = {"tp": 0, "fp": 0, "tn": 0, "fn": 0}

        default_negative_labels = ["Other", "Unknown", ".", "", " "]

        output_columns_names = output_df.columns.tolist()
        expected_columns_names = expected_df.columns.tolist()

        assert len(output_columns_names) == len(expected_columns_names)

        columns_size = len(output_columns_names)
        for i in range(0, columns_size, 1):
            expected = expected_columns_names[i]
            actual = output_columns_names[i]

            if expected == actual or (expected in default_negative_labels and actual in default_negative_labels):
                if expected == actual:
                    quality_dict["tp"] += 1
                elif expected in default_negative_labels and actual in default_negative_labels:
                    quality_dict["tn"] +=1
            else:
                if expected in default_negative_labels and actual not in default_negative_labels:
                    quality_dict["fp"] += 1
                elif expected not in default_negative_labels and actual in default_negative_labels:
                    quality_dict["fn"] += 1

        precision = quality_dict["tp"]/(quality_dict["tp"] + quality_dict["fp"])
        recall = quality_dict["tp"]/(quality_dict["tp"] + quality_dict["fn"])

        return {"p": precision, "r": recall, "f1": 2*(precision*recall)/(precision+recall)}
