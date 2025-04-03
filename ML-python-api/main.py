from fastapi import FastAPI, Body
from pydantic import BaseModel
import joblib
import pandas as pd

# The same 10 columns used in the dataset
FEATURE_COLS = [
    "hour",
    "after_hours_flag",
    "budget_deviation",
    "round_number_flag",
    "duplicate_flag",
    "supplier_change_count",
    "unexpected_supplier_flag",
    "supplier_frequency",
    "vendor_collusion_risk",
    "budget_dev_freq"
]

# Map model outputs back to readable strings
type_map_inv = {
    0: "Normal",
    1: "Duplicate Transaction",
    2: "Vendor Collusion",
    3: "Budget Overrun",
    4: "After-Hours Activity",
    5: "Round-Number Amount",
    6: "Unexpected Supplier"
}

severity_map_inv = {
    0: "None",
    1: "Low",
    2: "Medium",
    3: "High"
}

class TransactionFeatures(BaseModel):
    hour: float
    after_hours_flag: float
    budget_deviation: float
    round_number_flag: float
    duplicate_flag: float
    supplier_change_count: float
    unexpected_supplier_flag: float
    supplier_frequency: float
    vendor_collusion_risk: float
    budget_dev_freq: float

app = FastAPI()

# Load the two models and scaler
type_model = joblib.load("random_forest_type_model.joblib")
severity_model = joblib.load("random_forest_severity_model.joblib")
scaler = joblib.load("scaler.joblib")

# Numeric columns for scaling
NUMERIC_COLS = [
    "hour",
    "budget_deviation",
    "supplier_change_count",
    "supplier_frequency",
    "budget_dev_freq"
]

@app.post("/predict")
def predict_anomaly(transactions: list[TransactionFeatures] = Body(...)):
    """
    Accepts a list of transaction feature dictionaries,
    returns a list of anomaly type, severity, plus 'label' and 'score'
    for backward compatibility with the existing Java code.

    Example request body:
    [
      {
        "hour": 23,
        "after_hours_flag": 1,
        "budget_deviation": 3000.0,
        "round_number_flag": 0,
        "duplicate_flag": 0,
        "supplier_change_count": 2,
        "unexpected_supplier_flag": 0,
        "supplier_frequency": 10,
        "vendor_collusion_risk": 0,
        "budget_dev_freq": 30000.0
      }
    ]
    """

    # Convert input to DataFrame
    data_dicts = [t.dict() for t in transactions]
    df = pd.DataFrame(data_dicts, columns=FEATURE_COLS)

    # Scale numeric columns
    df[NUMERIC_COLS] = scaler.transform(df[NUMERIC_COLS])

    # Predict anomaly type
    type_preds = type_model.predict(df)  # e.g. [3, 0, 1, ...]
    # Predict severity
    sev_preds = severity_model.predict(df)  # e.g. [2, 0, 1, ...]

    results = []
    for i in range(len(df)):
        type_label_num = type_preds[i]
        severity_label_num = sev_preds[i]

        # Convert numeric labels to strings
        anomaly_type_str = type_map_inv.get(type_label_num, "Unknown")
        severity_str = severity_map_inv.get(severity_label_num, "Unknown")

        # Minimal logic to create 'label' and 'score'
        # Here, we treat "Normal" as label=0 => score=0.1
        # else label=1 => score=0.9
        if anomaly_type_str == "Normal":
            label = 0
            score = 0.1
        else:
            label = 1
            score = 0.9

        # Construct final object
        results.append({
            "label": label,
            "anomaly_type": anomaly_type_str,
            "severity": severity_str,
            "score": score
        })

    return {"predictions": results}
