use clap::{Arg, Command};
use rodio::{Decoder, OutputStream, Sink};
use serde::Deserialize;
use std::fs;
use std::fs::File;
use std::io::BufReader;
use walkdir::WalkDir;

#[derive(Deserialize)]
struct Settings {
    music_directory: String,
}

#[derive(Deserialize)]
struct Config {
    settings: Settings,
}

fn main() {
    println!("Hello, world!");
    let matches = Command::new("Music Play")
        .version("1.0")
        .about("Plays music")
        .arg(
            Arg::new("config")
                .short('c')
                .long("config")
                .value_name("CONFIG")
                // .about("Sets the configuration file to use")
                // .takes_values(true)
                .required(true),
        )
        .get_matches();

    let config_path = matches
        .get_one::<String>("config")
        .expect("No config file provided");
    let config_content =
        fs::read_to_string(config_path).expect("Failed to read the configuration file.");
    let config: Config =
        toml::from_str(&config_content).expect("Failed to parse the configuration file.");

    let directory = &config.settings.music_directory;
    let mut files = Vec::new();

    for entry in WalkDir::new(directory).into_iter().filter_map(|e| e.ok()) {
        if entry.path().is_file() {
            files.push(entry.path().to_path_buf());
        }
    }

    if files.is_empty() {
        eprintln!("No files found");
        return;
    }

    let (_stream, stream_handle) = OutputStream::try_default().unwrap();
    let sink = Sink::try_new(&stream_handle).unwrap();

    for file in files {
        if let Ok(file) = File::open(&file) {
            let source = Decoder::new(BufReader::new(file)).unwrap();
            sink.append(source);
        }
    }

    sink.sleep_until_end();
}
