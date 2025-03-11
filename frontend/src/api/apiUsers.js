import ky from "ky";

const apiUsers = ky.create({
  prefixUrl: "http://172.191.132.105:9091",
  hooks: {
    beforeRequest: [
      (request) => {
        const token = localStorage.getItem("token");
        if (token) {
          request.headers.set("Authorization", `Bearer ${token}`);
        } else {
          console.error("⚠️ No hay token en localStorage!");
        }
      },
    ],
  },
});

export default apiUsers;
