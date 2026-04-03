import os

ROOT_DIR = r"c:\Users\Admin\Desktop\thinkora-app-android"
OLD_PACKAGE = "com.apachi.thinkora"
NEW_PACKAGE = "com.apachi.auraskin"

def replace_in_files():
    for root, dirs, files in os.walk(ROOT_DIR):
        if ".git" in root or ".gradle" in root or ".idea" in root or "\\build" in root or "/build" in root:
            continue
        for file in files:
            if file.endswith(('.kt', '.xml', '.gradle', '.kts', '.pro', '.md')):
                filepath = os.path.join(root, file)
                try:
                    with open(filepath, 'r', encoding='utf-8') as f:
                        text = f.read()
                    
                    new_text = text.replace(OLD_PACKAGE, NEW_PACKAGE)
                    new_text = new_text.replace("com/apachi/thinkora", "com/apachi/auraskin")
                    new_text = new_text.replace("ThinkoraTextField", "AuraTextField")
                    new_text = new_text.replace("ThinkoraButton", "AuraButton")
                    new_text = new_text.replace("ThinkoraTopAppBar", "AuraTopAppBar")
                    
                    if new_text != text:
                        with open(filepath, 'w', encoding='utf-8') as f:
                            f.write(new_text)
                except Exception as e:
                    pass

def rename_directories():
    for root, dirs, files in os.walk(ROOT_DIR, topdown=False):
        for dir in dirs:
            if dir == "thinkora":
                if os.path.basename(root) == "apachi":
                    old_path = os.path.join(root, dir)
                    new_path = os.path.join(root, "auraskin")
                    print(f"Renaming {old_path} to {new_path}")
                    os.rename(old_path, new_path)

if __name__ == "__main__":
    replace_in_files()
    rename_directories()
