import Spark_md5 from 'spark-md5';

export function setRepository(repository) {
  localStorage.setItem("repository", JSON.stringify(repository));
}

export function getRepository(repository) {
  return JSON.parse(localStorage.getItem("repository"));
}

export function setUser(user) {
  localStorage.setItem("user", JSON.stringify(user));
}

export function getUser() {
  return JSON.parse(localStorage.getItem("user"));
}

export function getMd5BigFile(file, next) {
  let chunkSize = 1024 * 1024 * 5,
    chunks = Math.ceil(file.size / chunkSize),
    currentChunk = 0,
    sparkMd5 = new Spark_md5(),
    fileReader = new FileReader();
  fileReader.onload = function (event) {
    sparkMd5.appendBinary(event.target.result);
    currentChunk += 1;
    if (currentChunk < chunks) {
      loadNext();
    } else {
      const md5 = sparkMd5.end();
      next(md5);
    }
  };

  function loadNext() {
    let start = currentChunk * chunkSize,
      end = start + chunkSize >= file.size ? file.size : start + chunkSize;
    fileReader.readAsBinaryString(File.prototype.slice.call(file, start, end));
  }

  loadNext();
}

export function getMd5SmallFile(file, next) {
  const sparkMd5 = new Spark_md5();
  let reader = new FileReader();
  reader.onload = function (event) {
    const md5 = sparkMd5.appendBinary(event.target.result).end();
    next(md5);
  };
  reader.readAsBinaryString(file);
}


export function getMd5SliceFile(slice) {
  const sparkMd5 = new Spark_md5();
  return sparkMd5.appendBinary(slice.target.result).end();
}
