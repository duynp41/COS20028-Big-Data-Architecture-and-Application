def load_lng_codes(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        return set(line.strip().split('\t')[0] for line in f if line.strip())

def check_rel_code_synonym(rel_file, valid_codes_file):
    valid_codes = load_lng_codes(valid_codes_file)
    invalid_entries = []

    with open(rel_file, 'r', encoding='utf-8') as f:
        for idx, line in enumerate(f, 1):
            if not line.strip():
                continue
            parts = line.strip().split('\t')
            if len(parts) >= 1:
                lng_code = parts[0].strip()
                if lng_code and lng_code not in valid_codes:
                    invalid_entries.append((idx, lng_code, line.strip()))

    return invalid_entries

# === Replace these paths with your actual files ===
rel_code_synonym_path = 'rel_code_synonym_data/part-r-00000'
lng_id_path = 'lng_id_data/part-r-00000'

problems = check_rel_code_synonym(rel_code_synonym_path, lng_id_path)

if problems:
    print("Foreign key violation entries found:")
    for line_no, code, content in problems:
        print(f"Line {line_no}: {code} -> {content}")
else:
    print("✅ All lng_code values are valid and exist in lng_id.")
