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
