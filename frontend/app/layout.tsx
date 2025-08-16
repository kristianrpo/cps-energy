import "@/styles/globals.css";
import { Space_Grotesk } from "next/font/google";

const spaceGrotesk = Space_Grotesk({
  subsets: ["latin"],
});

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${spaceGrotesk.className} antialiased bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900 text-zinc-200 min-h-screen`}
      >
        {children}
      </body>
    </html>
  );
}
